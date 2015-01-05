
var sqrm = sqrm || {};

(function(sqrm) {

   var URL = "/sqrm/rest/";
   var USERNAME = "userA";
   var PASSWORD = "abc123";
   
   sqrm.submitForm = function(formName)
   {
      try
      {
         console.log("submitForm : " + formName);
         var frm = $('#'+formName);
         var title = null;
         var revision = null;
         var body = frm.find("textarea").val();
         var note = null;
         var tags = [];
         var username = null;
         var password = null;
 
         frm.find(":input").map(function(i,e) {
            console.log(i);
            console.log(e);
            if ($(this).attr("type")=="hidden" )
            {
               if ($(this).attr("name")=="title")
               {
                  title = $(this).val();
               }
               else if ($(this).attr("name")=="revision")
               {
                  revision = $(this).val();
               }
            }
            else if ($(this).attr("type")=="text")
            {
               if ($(this).attr("name")=="title") title = $(this).val();
               else if ($(this).attr("name")=="note") note = $(this).val();
               else if ($(this).attr("name")=="username") username = $(this).val();
               else tags.push({ name: $(this).attr("name"), value: $(this).val() });
            }
            else if ($(this).attr("type")=="password")
            {
               password = $(this).val();
            }
         });

         console.log("title = " + title);
         console.log("revision = " + revision);
         console.log("body = ");
         console.log(body);
         console.log("note = " + note);
         console.log("tags = ");
         for ( var t=0 ; t<tags.length ; t++ ) console.log(tags[t]);
         console.log("username = " + username);
         console.log("password = " + password);
         
         if (username && password)
         {
            // login
            sqrm.login({
               username : username,
               password : password,
               success : function(data) {
                  console.log("login success");
                  console.log(data);
               },
               error : function(error) {
                  console.log("login failed");
                  console.log(error);
               }
            });
         }
         else if (revision==null)
         {
            // create
            sqrm.create({
               title : title,
               body : body,
               tags : tags,
               note : note,
               success : function(data) {
                  console.log("creation success");
                  console.log(data);
               },
               error : function(error) {
                  console.log("creation failed");
                  console.log(error);
               }
            });
         }
         else
         {
            // update
            sqrm.update({
               title : title,
               revision : revision,
               body : body,
               tags : tags,
               note : note,
               success: function(data) {
                  console.log("updpate success");
                  console.log(data);
               },
               error : function(error) {
                  console.log("update failed");
                  console.log(error);
               }
            });
         }
      }
      catch (e)
      {
         console.log(e);
      }
   };

   sqrm.open = function(link,target)
   {
      if (!target) window.location = link;
   };

   sqrm.get = function(params)
   {
     $.ajax({
       type: 'GET',
       url: URL + "wiki/"+params.title,
       dataType: 'json',
       error: function(request,textStatus,error) {
          if (params.error)
          {
             try
             {
                params.error(error);
             }
             catch (e)
             {
                console.log(e);
             }
          }
          else
             {
             console.log("get " + params.title + " failed: " + textStatus);
             console.log(error);
             }
       },
       success: function(data,textStatus,request) {
          if (params.success)
          {
             try
             {
                params.success(data);
             }
             catch (e)
             {
                console.log(e);
             }
          }
          else
          {
          console.log("get " + params.title + " succeeded: " + textStatus);
          }
       }
     });
   };

   sqrm.create = function(params)
   {      
      var request = JSON.stringify({
         title: params.title,
         body: params.body,
         tags: params.tags,
         note: params.note });
      
      $.ajax({
         url: URL + "wiki",
         type: "POST",
         data: request,
//         username: USERNAME,
//         password: PASSWORD,
         contentType: "application/json; charset=utf-8",
         dataType: "json",
         error: function(request,textStatus,error) {
            if (params.error)
            {
               try
               {
                  params.error(error);
               }
               catch (e)
               {
                  console.log(e);
               }
            }
         },
         success: function(data,textStatus,request) {
            if (params.success)
            {
               try
               {
                  params.success(data);
               }
               catch (e)
               {
                  console.log(e);
               }
            }
         }
      });
   };

   sqrm.update = function(params)
   {
      var request = JSON.stringify({
         title: params.title,
         body: params.body,
         tags: params.tags,
         note: params.note });
      
      $.ajax({
         url: URL + "wiki/"+params.title,
         type: "PUT",
//         username: USERNAME,
//         password: PASSWORD,
         data: request,
         contentType: "application/json; charset=utf-8",
         dataType: "json",
         error: function(request,textStatus,error) {
            if (params.error)
            {
               try
               {
                  params.error(error);
               }
               catch (e)
               {
                  console.log(e);
               }
            }
         },
         success: function(data,textStatus,request) {
            if (params.success)
            {
               try
               {
                  params.success(data);
               }
               catch (e)
               {
                  console.log(e);
                  console.log(params.success);
                  console.log(data);
               }
            }
         }
      });
   
   };

   sqrm.del = function(params)
   {
     $.ajax({
       type: 'delete',
       url: URL + "wiki/"+params.title,
       dataType: 'json',
//       username: USERNAME,
//       password: PASSWORD,
       error: function(request,textStatus,error) {
          if (params.error)
          {
             try
             {
                params.error(error);
             }
             catch (e)
             {
                console.log(e);
             }
          }
       },
       success: function(data,textStatus,request) {
          if (params.success)
          {
             try
             {
                params.success(data);
             }
             catch (e)
             {
                console.log(e);
             }
          }
       }
     });
   };

   sqrm.search = function(params)
   {
     $.ajax({
       type: 'get',
       url: URL + "search?q="+params.q,
       dataType: 'json',
       error: function(request,textStatus,error) {
          if (params.error)
          {
             try
             {
                params.error(error);
             }
             catch (e)
             {
                console.log(e);
             }
          }
       },
       success: function(data,textStatus,request) {
          if (params.success)
          {
             try
             {
                params.success(data);
             }
             catch (e)
             {
                console.log(e);
             }
          }
       }
     });
   };

   sqrm.login = function(params)
   {
      console.log(params);
      $.ajax({
         type: 'POST',
         url: URL + "/user/login",
         data: JSON.stringify({username: params.username, password: params.password}),
         contentType: "application/json; charset=utf-8",
         dataType: "json",
         error: function(request,textStatus,error) {
            if (params && params.error)
            {
               try
               {
                  params.error(error);
               }
               catch (e)
               {
                  console.log(e);
               }
            }
            else console.log("login error");
         },
         success: function(data,textStatus,request) {
            if (params && params.success)
            {
               try
               {
                  params.success(data);
               }
               catch (e)
               {
                  console.log(e);
               }
            }
            else console.log("login success");
         }
       });
      
   };
   
   sqrm.logout = function(params)
   {
      $.ajax({
         type: 'POST',
         url: URL + "/user/logout",
         error: function(request,textStatus,error) {
            if (params && params.error)
            {
               try
               {
                  params.error(data);
               }
               catch (e)
               {
                  console.log(e);
               }
            }
            else console.log("logout failed");
         },
         success: function(data,textStatus,request) {
            if (params && params.success)
            {
               try
               {
                  params.success(data);
               }
               catch (e)
               {
                  console.log(e);
               }
            }
            else console.log("logout successful");
         }
       });
   };
})(sqrm);
