$(document).ready(function($){
     $('input[name="today"]').on('change', function(){
       if($(this).is(':checked')){
         document.cookie = "today=on";
         window.location='/archinterest/news';
       } else {
         document.cookie = "today=off";
         window.location='/archinterest/news';
       }
     });
   });