

	//var context = document.getElementById("my_div");
	//var instance = new Mark(context);
	//var options = {};

	//$(".context").mark(keyword, options);
	$(document).ready(function(){
    $("#id_pulsante").click(function(){

        //window.alert("ciao ciccio");
        $("my_div").mark("Pasticcio");

        //$(".context").mark("Ciccio"); // will mark the keyword "Ciccio", requires an element with class "context" to exist
        //instance.mark("Ciccio", options);
    });
})    