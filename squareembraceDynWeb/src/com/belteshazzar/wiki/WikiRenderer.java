package com.belteshazzar.wiki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.UUID;
import java.util.Vector;

public class WikiRenderer
{
	private class IL
	{
		int indenting;
		String text;

		public IL(int indenting, String text)
		{
			this.indenting = indenting;
			this.text = text;
		}
	}

	BufferedReader in;
	PrintStream out;

	String line;
	int lineIndent;
	String next;
	int nextIndent;
	String divId = "";
	String divClass = "";

	public WikiRenderer(BufferedReader in, PrintStream out)
	{
		this.in = in;
		this.out = out;

		IL l = readLine(0);
		line = l.text;
		lineIndent = l.indenting;

		IL n = readLine(0);
		next = n.text;
		nextIndent = n.indenting;
		divId = "wiki";

		process();
	}

	private void process()
	{
		char ch;
		int indenting = lineIndent;

		out.println("<DIV ID=\"" + divId + "\"" + divClass + ">");
		divId = "";
		divClass = "";

		while (line != null && lineIndent >= indenting)
		{
			if (lineIndent > indenting)
			{
				process();
			}
			else if (line.length() > 0)
			{
				System.err.println(line);
				ch = line.charAt(0);
				switch (ch)
				{
				// case '#': processTag();
				// break;
					case '=':
						processHeading();
						break;
					case '*':
						processList();
						break;
					case '|':
						processTable();
						break;
					case '+':
						processDiv();
						break;
					default:
						out.println("<P>" + format(line) + "</P>");
						nextLine();
				}
			}
			else
			{
				nextLine();
			}
		}
		out.println("</DIV>");
	}

	private void processDiv()
	{
		if (nextIndent > lineIndent)
		{
			String[] div = line.substring(1).split("\\s+", 2);
			divId = div[0];
			divClass = (div.length == 2 ? " CLASS=\"" + div[1] + "\"" : "");
		}
		nextLine();
	}

	private void processList()
	{
		processList(1);
	}

	private void processList(int lvl)
	{
		int indenting = lineIndent;
		boolean inli = false;
		out.print("<OL>");

		while (line != null && line.length() > 0 && line.charAt(0) == '*'
				&& lineIndent == indenting)
		{
			int starCount = 0;
			while (starCount < line.length() && line.charAt(starCount) == '*')
				starCount++;
			if (starCount > lvl)
			{
				processList(lvl + 1);
				if (inli)
				{
					out.print("</LI>");
					inli = false;
				}
			}
			else if (starCount < lvl)
			{
				break;
			}
			else
			{
				if (inli) out.print("</LI>");
				out.print("<LI>" + format(line.substring(lvl).trim()));
				inli = true;
				nextLine();
			}
		}

		if (inli) out.print("</LI>");
		out.println("</OL>");
	}

	private int formNumber = 0;

	private void processTable()
	{
		int indenting = lineIndent;
		Vector<String[]> rows = new Vector<String[]>();
		String id = UUID.randomUUID().toString().replace('-', '_');

		while (line != null && line.length() > 0 && line.charAt(0) == '|'
				&& lineIndent == indenting)
		{
			rows.add(line.substring(1).split("\\|"));
			nextLine();
		}

		out.println("<FORM id=\"form" + formNumber + "\" onsubmit=\"if (cm"
				+ id + ") cm" + id + ".save(); sqrm.submitForm('form"
				+ formNumber + "'); return false;\" >");
		out.println("<TABLE>");
		for (String[] cells : rows)
		{
			out.println("<TR>");
			if (cells.length > 0 && cells[0].length() > 1
					&& cells[0].charAt(0) == '?')
			{
				String formType = cells[0].substring(1).trim().toLowerCase();
				String label = (cells.length > 1 ? format(cells[1]) : formType);
				String value = (cells.length > 2 ? cells[2].trim() : "");
				String placeholder = (cells.length > 3 ? cells[3].trim() : "");

				out.print("<TD>");
				out.print(label);
				out.print("</TD>");

				out.print("<TD>");
				switch (formType)
				{
					case "title":
						if (!value.equals(""))
						{
							out.print(value);
							out.print("<input type=\"hidden\" name=\"title\" value=\""
									+ value + "\" />");
						}
						else
						{
							out.print("<input type=\"text\" name=\"title\" value=\"\" placeholder=\""
									+ placeholder + "\"/>");
						}
						break;
					case "revision":
						out.print("<input type=\"hidden\" name=\"revision\" value=\""
								+ value + "\" />");
						break;
					case "body": // TODO: should only allow one body or one css
					case "css":
						out.print("<textarea id=\"" + id + "\" name=\""
								+ formType + "\">");
						out.print(value.replaceAll("<BR>", "\n"));
						out.print("</textarea>");
						out.print("<script>");
						out.print("var cm"
								+ id
								+ " = CodeMirror.fromTextArea(document.getElementById(\""
								+ id
								+ "\"), {lineNumbers: true, mode: \"text/x-csrc\" });");
						out.print("</script>");
						break;
					case "password":
						out.print("<input type=\"password\" value=\"" + value
								+ "\"  placeholder=\"" + placeholder + "\"/>");
						break;
					case "submit":
						out.print("<input type=\"submit\" value=\"" + value
								+ "\" />");
						break;
					case "reset":
						out.print("<input type=\"reset\" value=\"" + value
								+ "\" />");
						break;
					case "note":
					default:
						out.print("<input type=\"text\" name=\"" + formType
								+ "\" value=\"" + value + "\"  placeholder=\""
								+ placeholder + "\"/>");
						break;
				}
				out.print("</TD>");
			}
			else
			{
				for (String cell : cells)
				{
					if (cell.length() > 1 && cell.charAt(0) == '!')
					{
						out.print("<TH>" + format(cell.substring(1)) + "</TH>");
					}
					else
					{
						out.print("<TD>" + format(cell) + "</TD>");
					}
				}
			}
			out.println();
			out.println("</TR>");

		}
		out.println("</TABLE>");
		out.println("</FORM>");

		formNumber++;
	}

	// private void processTag()
	// {
	// String[] tag = line.substring(1).split("\\s+",2);
	// System.err.println("WikiRenderer.processTag: " + line);
	// if (tag[0].charAt(0)!='!')
	// {
	// if (tag.length==2)
	// {
	// out.print("<script>document."+tag[0]+" = \""+tag[1]+"\";</script>");
	// }
	// else
	// {
	// out.print("<script>document."+tag[0]+" = true;</script>");
	// }
	// }
	// else
	// {
	// tag[0] = tag[0].substring(1);
	// out.print("<script>"+tag[0]+"(");
	// if (tag.length==2)
	// {
	// String[] params = tag[1].split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	// List<String> cleanedParams = new LinkedList<String>();
	// for (int i=0 ; i<params.length ; i++)
	// {
	// String param = params[i].trim();
	// if (param.length()>0) cleanedParams.add(param);
	// }
	// for (int i=0 ; i<cleanedParams.size()-1 ; i++)
	// {
	// out.print(tagParam(cleanedParams.get(i))+",");
	// }
	// if (cleanedParams.size()>0)
	// out.print(tagParam(cleanedParams.get(cleanedParams.size()-1)));
	// }
	// out.println(");</script>");
	// }
	// nextLine();
	// }

	private void processHeading()
	{
		int h = 1;
		for (; h < line.length() && line.charAt(h) == '='; ++h)
			;
		out.println("<H" + h + ">" + format(line.substring(h).trim()) + "</H"
				+ h + ">");
		nextLine();
	}

	private String format(String s)
	{
		StringBuilder out = new StringBuilder();
		format(s, 0, out, Character.MAX_VALUE);
		return out.toString();
	}

	private String tagFor(char c)
	{
		switch (c)
		{
			case '!':
				return "bold";
			case '~':
				return "italic";
			case '_':
				return "underline";
			case '-':
				return "line-through";
			case '.':
				return "subscript";
			case '^':
				return "superscript";
			default:
				return c + "";
		}
	}

	private String link(String s)
	{
		String[] parts = s.split("\\|", 2);
		if (parts.length == 1) return "<A HREF=\"" + url(parts[0]) + "\">"
				+ parts[0] + "</A>";
		else return "<A HREF=\"" + url(parts[0]) + "\">" + parts[1] + "</A>";
	}

	private String url(String s)
	{
		return s;
	}

	private int format(String s, int i, StringBuilder out, char in)
	{
		char a, b;

		while (i < s.length())
		{
			a = s.charAt(i++);
			if (a == '[')
			{
				for (int j = i; j < s.length(); j++)
				{
					if (s.charAt(j) == ']')
					{
						out.append(link(s.substring(i, j)));
						i = j + 1;
						if (i >= s.length())
						{
							if (in != Character.MAX_VALUE) out
									.append("</SPAN>");
							return i;
						}
						a = s.charAt(i++);
						break;
					}
				}
			}
			if (i + 1 > s.length())
			{
				if (in != Character.MAX_VALUE) out.append("</SPAN>");
				out.append(a);
				return i;
			}
			b = s.charAt(i);
			if (a == b
					&& (a == '!' || a == '~' || a == '_' || a == '-'
							|| a == '.' || a == '^'))
			{
				i++;
				if (a == in)
				{
					out.append("</SPAN>");
					return i;
				}
				else
				{
					if (a == '.' && s.length() > i && s.charAt(i) == '.')
					{
						out.append('.');
						out.append('.');
						while (i < s.length() && s.charAt(i) == '.')
						{
							i++;
							out.append('.');
						}
					}
					else
					{
						out.append("<SPAN class=\"" + tagFor(a) + "\">");
						i = format(s, i, out, a);
					}
				}
			}
			else
			{
				out.append(a);
			}
		}
		if (in != Character.MAX_VALUE) out.append("</SPAN>");
		return i;
	}

	// private String tagParam(String s)
	// {
	// if ( s.charAt(0)=='"' && s.charAt(s.length()-1)=='"')
	// {
	// return s;
	// }
	//
	// try
	// {
	// Double.parseDouble(s);
	// return s;
	// }
	// catch (NumberFormatException nfex)
	// {
	// return '"' + s + '"';
	// }
	// }

	private void nextLine()
	{
		// if (nextIndent>lineIndent)
		// {
		// line = "";
		// lineIndent = nextIndent;
		// }
		// else if (nextIndent<lineIndent)
		// {
		// line = "";
		// lineIndent = nextIndent;
		// }
		// else
		// {
		line = next;
		lineIndent = nextIndent;

		if (next != null)
		{
			IL n = readLine(lineIndent);
			next = n.text;
			nextIndent = n.indenting;
		}
		// }
	}

	private IL readLine(int defaultIndenting)
	{
		String line = null;

		try
		{
			while (true)
			{
				line = in.readLine();
				if (line == null)
				{
					return split(line, defaultIndenting);
				}

				while (line.length() > 0
						&& line.charAt(line.length() - 1) == '\\')
				{
					String nextLine = in.readLine();
					if (nextLine == null)
					{
						line = line.substring(0, line.length() - 1);
						return split(line, defaultIndenting);
					}

					line = line.substring(0, line.length() - 1) + nextLine;
				}

				if (line.matches("^\\s*$"))
				{
					continue;
				}
				return split(line, defaultIndenting);
			}
		}
		catch (IOException e)
		{
			return split(null, defaultIndenting);
		}
	}

	public IL split(String line, int defaultIndenting)
	{
		if (line == null)
		{
			return new IL(0, null);
		}
		else if (line.length() == 0)
		{
			return new IL(defaultIndenting, "");
		}
		else
		{
			int i = 0;
			while (i < line.length()
					&& (line.charAt(i) == ' ' || line.charAt(i) == '\t'))
				i++;
			return new IL(i, line.substring(i));
		}
	}
}
