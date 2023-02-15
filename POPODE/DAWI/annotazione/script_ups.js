// **** CARICAMENTO DELLE UNIT AL CARICAMENTO DELLA PAGINA ****


// [{"id":"1","print":"Unit1"},{"id":"2","print":"Unit2"},{"id":"3","print":"Unit3"},{"id":"4","print":"Unit4"},{"id":"5","print":"Unit 5"}]
function get_units_select () {

        get_text();

        //var unit_choice = document.getElementById("unitchoice").value;

		//document.getElementById('test').innerHTML = "hai selezionato: " + candidate_unit;		
		if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
		xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	var response = xmlhttp.responseText;         	
            	
            	var my_response = JSON.parse(xmlhttp.responseText);              	
            	var sel = document.getElementById("id_select_unit");
            	var istr = document.createElement("option");

            	istr.text = "select a unit to edit";
            	sel.add(istr);           	
          		
          		//Cancello il precedente menu
          		//removeOptions(document.getElementById("id_select_unit"));          		
            	my_response.forEach(function(entry) {
    				var id = entry.id;
    				var printAction = entry.print;
    				var option = document.createElement("option");
                    var reference = entry.ref;
                    if (reference == "reference") {printAction = printAction + " [R]"}
					option.text = printAction;
					option.value = id;
					sel.add(option);
					});	

                //get_text();
	 		}           
        }
        xmlhttp.open("GET","get_candidate_units.php",false);
        xmlhttp.send();		
}	


//$().highlight() 
function getSelectedText(el) {
    var sel, range;
    if (typeof el.selectionStart == "number" && typeof el.selectionEnd == "number") {
        //return el.value.slice(el.selectionStart, el.selectionEnd);
        var sliced = el.value.slice(el.selectionStart, el.selectionEnd);
        var boundaries = el.selectionStart + " - " + el.selectionEnd;

        //$("#id_bind").highlight('BARNARDO');
        var selection = {start:el.selectionStart, end:el.selectionEnd, text:sliced};
        return selection;
    } else if (
            (sel = document.selection) &&
            sel.type == "Text" &&
            (range = sel.createRange()).parentElement() == el) {
         return range.text;
    }
    return "";
}

function get_text (){
        $.get("get_text.php", function(data, status){
        $("#id_textarea").text(data);

        //$("#id_textarea").load('Texts/sample.html');
        });
}

/*
$.get( "ajax/test.html", function( data ) {
  $( ".result" ).html( data );
  alert( "Load was performed." );
});
*/


$(document).ready(function(){
    $("#id_bind").click(function(){

        
        var el = document.getElementById("id_textarea");
        var unit = $("#id_select_unit").val();
        var sel = getSelectedText(el);
        var text = sel.text;
        var start = sel.start;
        var end = sel.end;

        //window.alert(sel.text + " - " + sel.start + " - " + sel.end);

        if((unit != "select a unit to edit") && (text != "")) {

            //$.getJSON("bind_text.php?text="+"ciao ciccio ciccio"+"&start="+start+"&end="+end+"&unit="+unit, function(data, status){
                //scrivi nel div del binding
           
             //   $("#id_binding").val(text);

                   
            $.post("bind_text.php", { text:text, unit:unit, start:start, end:end } ,function(data){
                //window.alert("selected text: " + start + " " + end);
                $("#id_binding").val(text);
                });


            } else {
                    window.alert("please select a unit and some text");
                    }
    });
})    


/*
function getSelectedText(){
    var t = '';
    if(window.getSelection){
        t = window.getSelection();
    }else if(document.getSelection){
        t = document.getSelection();
    }else if(document.selection){
        t = document.selection.createRange().text;
    }
    return t;
}
*/


// 
// id_button_previous_unit id_button_next_unit

$(document).ready(function(){
    $("#id_button_previous_unit").click(function(){

		var previous_unit = $("#candidate_previous").val();
		var unit_corrente = $("#id_select_unit").val();
		var timeline_corrente = $("#id_timeline_corrente").val();
		changePair(previous_unit,unit_corrente,id_timeline_corrente,'pre'); 
        $("#candidate_previous").prop('selectedIndex',0);
    });
})    



$(document).ready(function(){
    $("#id_button_next_unit").click(function(){

		var previous_unit = $("#candidate_next").val();
		var unit_corrente = $("#id_select_unit").val();
		var timeline_corrente = $("#id_timeline_corrente").val();
		changePair(previous_unit,unit_corrente,id_timeline_corrente,'eff'); 
		$("#candidate_next").prop('selectedIndex',0);
    });
})    


// ******** CARICAMENTO DELLA UNIT E DEI SUOI PIANI ***********


// alla selezione della unit, scatta la selezione dei piani collegati
// creazione del menu dei piani
// id_select_plan
// get_deplans.php
// [{"id":"1","print":"Piano in cui Amleto ...","name":"Piano 1","type":"Base","timeline":null,"agent":"5","goal":"2","action":"1"},
// "id":"3","print":"Piano di Ofelia per fuggire a quella gabbia di pazzi","name":"Piano 2","type":"Rec","timeline":null,"agent":null,"goal":"2","action":"1"}]


$(document).ready(function(){
	
//mettere il check che non ci sia "select a plan" selezionato!

	
    $("#id_select_unit").change(function(){ 	
        //window.alert("faccio selezione unit");

        // all'onchange della unit
        /*
        document.getElementById('check').value = 'ok'; 
        clear_pagina(); setCurrentUnit(this.value); 
        var id_unit_corrente = document.getElementById('id_unit_corrente').value; 
        var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; 
        get_all(this.value,id_timeline_corrente);
        */

        /*
        var unit_type = $("#type_unit_corrente").val();
            var plan_type = "Base"; 
            //"Rec" | "Base"

            if (unit_type == "annotation") {plan_type="Rec";}
        */
    	    	
    	var unit = $("#id_select_unit").val();

        if (unit != "select a unit to edit"){
            //window.alert("entro in setting unit");
            //window.alert ("unit vale: " + unit);

        document.getElementById('check').value = 'ok'; 
        clear_pagina(); 
        setCurrentUnit(unit); 
        var id_unit_corrente = document.getElementById('id_unit_corrente').value; 
        var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; 
        get_all(unit,id_timeline_corrente);

        // prelevo i values disponibili
        refreshUnitValues();

        var unit_type = $("#type_unit_corrente").val();
        //Carico tutti i piani: candidati
        var plan_type = "Base"; 
        //"Rec" | "Base"

        if (unit_type == "annotation") {plan_type="Rec";}
        get_candidate_plans (unit,plan_type);


        //ed effettivi
        $.getJSON("get_unit_plans.php?unit="+unit, function(data, status){
        	$("#id_select_plan").empty();
        	var init =  document.createElement("option");
        	$(init).text("Select a plan");
            $(init).val("");
            $("#id_select_plan").append(init);
        	
            $.each(data, function(i, field){
            	//qui scorri l'array del piano per generare testo
            	var s = document.createElement("option");
            	$(s).text(field.name);
            	$(s).val(field.id);
            	$("#id_select_plan").append(s);
        	}) 


        	
         	// si azzera tutta la parte inferiore della pagina:
         	// azzero gli agenti
         	$("#id_name_agent_corrente").html('___'); 
         	$("#id_description_agent_corrente").html("___"); 
         	// azzero i goal
         	$("#id_description_goal_corrente").html("___");
        	$("#id_name_goal").html("___");
        	// azzero le actions
        	//document.getElementById('name_incident').value=''; 	
        	//document.getElementById('id_print_incident').value=''; 
        	//document.getElementById('id_name_action_corrente').innerHTML = '____';
        	//document.getElementById('id_description_action_corrente').innerHTML = '____';
    		// azzero il piano
			document.getElementById('id_name_deplan_corrente').innerHTML = '____';
			document.getElementById('description_deplan_corrente').innerHTML = '____';
            document.getElementById('name_action').innerHTML = '____';
           

    		
         		
        	  

        });
        } else {location.reload();}
    });
});


// ****** refresh values unit ************


// modificare perché legga VAS della unit data

function refreshUnitValues (){

            // prima memorizzo i valori correnti
            // id_value_atStake_before
            // id_value_inBalance_before
            var unit = $("#id_select_unit").val();
            var timeline_corrente = $("#id_timeline_corrente").val();

            var current_atstake_unit_b = $("#id_value_atStake_before").val();
            var current_balanced_unit_b = $("#id_value_inBalance_before").val();
            var current_atstake_unit_a = $("#id_value_atStake_after").val();
            var current_balanced_unit_a = $("#id_value_inBalance_after").val();



            // recupero tutti i valori di tutti gli agenti
            // e genero i menu
            $.getJSON("get_VAS_unit.php?unit="+unit+"&timeline="+timeline_corrente, function(data, status){
                //restituisce un array di 4 elementi, ognuno contenente un array di values con le caratteristiche indicate
                //{"VAS_before":[],"VAB_before":[{"id_state":"8364","id_agent":"125","id_value":"79","name_value":"cioccolata"}],
                //"VAS_after":[{"id_state":"8363","id_agent":"125","id_value":"78","name_value":"caff\u00e8"}],"VAB_after":[]}
                //qui ci serve solo l'id del valore, cioè id_value

                // vuoto i quattro menu
                $("#id_value_atStake_before").empty();
                $("#id_value_inBalance_before").empty();
                $("#id_value_atStake_after").empty();
                $("#id_value_inBalance_after").empty();
                

                //VAS_before    
                //{"id_state":"8419","id_agent":"131","id_value":"114","name_value":"brassicacee"},
                //{"id_state":"8418","id_agent":"128","id_value":"113","name_value":"amicizia"}            
                // ripetere per tutti i values per recuperare i valori effettivi

                var VAS_before = data.VAS_before;
                var VAB_before = data.VAB_before;
                var VAS_after = data.VAS_after;
                var VAB_after = data.VAB_after;
                //if (VAS_before != null) {var VAS_before_id = VAS_before.id_value;} else {var VAS_before_id = null;}

                //creo un array di values_id
                var i;
                var VAS_before_ids = [];
                var VAB_before_ids = [];
                var VAS_after_ids = [];
                var VAB_after_ids = [];


                for(i=0; i<VAS_before.length; i++)
                {
                    VAS_before_ids.push(VAS_before[i].id_value);
                }

               // window.alert(VAS_before_ids);                
                
                
               
                //if (VAB_before != null) {var VAB_before_id = VAB_before.id_value;} else {var VAB_before_id = null;}
                for(i=0; i<VAB_before.length; i++)
                {
                    VAB_before_ids.push(VAB_before[i].id_value);
                }

                
                
                //if (VAS_after != null) {var VAS_after_id = VAS_after.id_value;} else {var VAS_after_id = null;}
                for(i=0; i<VAS_after.length; i++)
                {
                    VAS_after_ids.push(VAS_after[i].id_value);
                }
                
                
                
                //if (VAB_after != null) {var VAB_after_id = VAS_after.id_value;} else {var VAB_after_id = null;}
                for(i=0; i<VAB_after.length; i++)
                {
                    VAB_after_ids.push(VAS_after[i].id_value);
                }
                
                //window.alert( VAS_before_id + " - " +  VAB_before_id  + " - " +  VAS_after_id  + " - " +  VAB_after_id);


                // ora ricreo e setto tutti i menu
                // verifica che funzioni anche per il values messi a 0
                $.getJSON("get_values.php", function(data, status){ 

                    /*
                    var s0 = document.createElement("option");
                    $(s0).text("no value");
                    $(s0).val("no");
                    $("#id_value_atStake_before").append(s0);
                    */

                    $.each(data, function(i, field){
                        //qui scorri l'array del piano per generare testo
                        var s = document.createElement("option");
                        var testo_select = field.name + " - " + field.nameAgent;
                        $(s).text(testo_select);
                        $(s).val(field.id);
                        $("#id_value_atStake_before").append(s);
                        //window.alert(field.id);

                        //ERO QUI    
                        //if (VAS_before_ids.includes(field.id)) {$("#id_value_atStake_before").val(field.id);}
                        $("#id_value_atStake_before").val(VAS_before_ids);
                    });
                    
                    /*
                    var s0 = document.createElement("option");
                    $(s0).text("no value");
                    $(s0).val("no");
                    $("#id_value_inBalance_before").append(s0);
                    */

                    $.each(data, function(i, field){
                        //qui scorri l'array del piano per generare testo
                        var s = document.createElement("option");
                        var testo_select = field.name + " - " + field.nameAgent;
                        $(s).text(testo_select);
                        $(s).val(field.id);
                        $("#id_value_inBalance_before").append(s);
                        //if (field.id == VAB_before_id) {$("#id_value_inBalance_before").val(VAB_before_id);}
                        $("#id_value_inBalance_before").val(VAB_before_ids);
                    });

                    /*
                    var s0 = document.createElement("option");
                    $(s0).text("no value");
                    $(s0).val("no");
                    $("#id_value_atStake_after").append(s0);
                    */

                    $.each(data, function(i, field){
                        //qui scorri l'array del piano per generare testo
                        var s = document.createElement("option");
                        var testo_select = field.name + " - " + field.nameAgent;
                        $(s).text(testo_select);
                        $(s).val(field.id);
                        $("#id_value_atStake_after").append(s);
                        //if (field.id == VAS_after_id) {$("#id_value_atStake_after").val(VAS_after_id);}
                        $("#id_value_atStake_after").val(VAS_after_ids);
                    });

                    /*
                    var s0 = document.createElement("option");
                    $(s0).text("no value");
                    $(s0).val("no");
                    $("#id_value_inBalance_after").append(s0);
                    */

                    $.each(data, function(i, field){
                        //qui scorri l'array del piano per generare testo
                        var s = document.createElement("option");
                        var testo_select = field.name + " - " + field.nameAgent;
                        $(s).text(testo_select);
                        $(s).val(field.id);
                        $("#id_value_inBalance_after").append(s);
                        //if (field.id == VAB_after_id) {$("#id_value_inBalance_after").val(VAB_after_id);}
                        $("#id_value_inBalance_after").val(VAB_after_ids);
                    });

                });
                
             });

}


// ***** CARICAMENTO DI AGENT E GOAL DEL PIANO ****

$(document).ready(function(){	
    $("#id_select_plan").change(function(){ 	    	
       var plan = $("#id_select_plan").val();
       var unit = $("#id_select_unit").val();
       //$("#id_binding").val(""); 
       //window.alert("piano selezionato: " + plan);
       
       if ((plan == "Select a plan") || (plan == "")) {
            clear_pagina_low(); 
            clear_box_plan(); 
        }
       
       else {
             
       $.getJSON("get_plan.php?plan="+plan, function(data, status){       
            
            $("#id_description_plan_corrente").text(data.print);
            var action = data.action;
            var agent = data.agent;
            var goal = data.goal;
            var plan = data.id;
            var accomplished = data.accomplished;
            var atstake_p = data.valueAtStakePlan_p;
            var balanced_p = data.valueBalancedPlan_p;
            var atstake_e = data.valueAtStakePlan_e;
            var balanced_e = data.valueBalancedPlan_e;
            var inconflict = data.inConflictWithPlan;
            var insupport = data.inSupportOfPlan;
            var action = data.action
           
            
            $("#id_plan_corrente").val(plan);
            $("#description_deplan_corrente").html(data.print);
            $("#id_name_deplan_corrente").html(data.name);
            
            getInfoAction (action) 
            
            //$("#name_action").html(data.action);
    	
    		//carico AGENTE nella descrizione dell'agente e lo setto come agente corrente  
    		//{"print":"il tranquillo","name":"Bromuro","id":"5"} 
    		$("#id_select_agent").prop('selectedIndex',0); 
    		if (agent != null){
    			$.getJSON("get_agent_info.php?agent="+agent, function(data, status){
    				//{"print":"...","name":"...","values":"...","id":"..."}
    				//scrivo il nome dell'agente
            		$("#id_description_agent_corrente").html(data.print);
            		//setto il menu corrispondente
            		//$("#id_select_agent").val(agent);
            		$("#id_name_agent_corrente").html(data.name);
            		$("#id_print_values").html(data.values);
                    //id_print_pleasant
                    //$("#id_print_pleasant").html(data.pleasant);
                    //id_print_like
                    $("#id_print_like").html(data.liking);
                    //id_print_dislike
                    $("#id_print_dislike").html(data.disliking);

                    //leggo plesant 
                    //window.alert(data.pleasant);
                    if (data.pleasant == 1) {
                        //window.alert("passo di qui");
                    $("#id_pleasant_yes").prop('checked', true);
                    $("#id_pleasant_no").prop('checked', false);
                    } else 
                        { 
                        $("#id_pleasant_yes").prop('checked', false);
                        $("#id_pleasant_no").prop('checked', true); 
                        }



        		});	
    		} else { 	//$("#id_select_agent").prop('selectedIndex',0); 
    					$("#id_name_agent_corrente").html('___'); $("#id_description_agent_corrente").html("___"); $("#id_print_values").html('___');}        
        	
        	//carico goal
        	//$("#id_select_goal").prop('selectedIndex',0);
        	if (goal != null){	
        		$.getJSON("get_goal_info.php?goal="+goal, function(data, status){
            		$("#id_description_goal_corrente").html(data.print);
            		$("#id_name_goal").html(data.name);
            		//$("#id_select_goal").val(goal);
        		});        	
        	} else { 	//$("#id_select_goal").prop('selectedIndex',0); 
        				$("#id_description_goal_corrente").html("___");
        				$("#id_name_goal").html("___");
        				}	
			
			// inzializzo anche accomplished (id_accomplished_yes/no)
			if (accomplished == 1) {
					$("#id_accomplished_yes").prop('checked', true);
					$("#id_accomplished_no").prop('checked', false);
					} else 
						{ 
						$("#id_accomplished_yes").prop('checked', false);
						$("#id_accomplished_no").prop('checked', true);	
						}
			
			// carico menu dei piani in conflitto/supporto
			// si devono estrarre tutti i piani e posizionare il menu su quello giusto
			// id_conflict_plan e id_support_plan
			$.getJSON("get_unit_plans.php?unit="+unit, function(data, status){
        		$("#id_conflict_plan").empty();
        		$("#id_support_plan").empty();
               	
               	/*
                var s0 = document.createElement("option");
        		$(s0).text("no plan");
        		$(s0).val("no");
        		$("#id_conflict_plan").append(s0);
                */
               	
            	$.each(data, function(i, field){

                    //controllo che l'id corrente sia diverso da plan
                    //per non mettere tra i candidati il piano stesso
                    //window.alert("piano corrente vale " + $(s).val(field.id) + "piano selezionato vale " + plan );
                    if ( field.id != plan ){
            		  //qui scorri l'array del piano per generare testo
            		  var s = document.createElement("option");
            		  $(s).text(field.name);
            		  $(s).val(field.id);
            		  //window.alert("questa voce di menu ha come id " + field.id);
            		  $("#id_conflict_plan").append(s);
                    }
        		});
        		
        		
        		
        		if (inconflict != 0) {
        		//window.alert("inconflict vale " + inconflict);
        		//$("#id_conflict_plan").prop('selectedIndex',inconflict);
        		$("#id_conflict_plan").val(inconflict);
        		} 
                else {
                    //window.alert("inconflict vale: " + inconflict) ;
                    //var target = $("#id_conflict_plan option[value=no]").val();
                    //window.alert ("il primo elemento vale " + target);
                    $("#id_conflict_plan").val("no");}
        		//else {$("#id_conflict_plan").prop('selectedIndex',0); }
                //else {$('#id_conflict_plan option[value=no]').attr('selected', 'selected');}
        		            	
               	/*
                var s0 = document.createElement("option");
        		$(s0).text("no plan");
        		$(s0).val("no");
        		$("#id_support_plan").append(s0);
                */
        		
        		$.each(data, function(i, field){

                    if ( field.id != plan ){
            		  //qui scorri l'array del piano per generare testo
            		  var s = document.createElement("option");
            		  $(s).text(field.name);
            		  $(s).val(field.id);
            		  $("#id_support_plan").append(s);
        		      }
                }) ;
        		
        		if (insupport != 0) {
        		$("#id_support_plan").val(insupport);
        		}
			});
						
						
						
			// carico menu dei values in conflitto/bilanciati
			// si devono estrarre tutti i valori e posizionare il menu su quello giusto
			// id_conflict_value id_support_value
      
      		//if (agent != "") {

            //ROSSANA MODIFICARE!!
            //FUNZIONA GIA'    
      
      		 $.getJSON("get_values.php?", function(data, status){
      			
      			$("#id_conflict_value_p").empty();
        		$("#id_support_value_p").empty();
                $("#id_conflict_value_e").empty();
                $("#id_support_value_e").empty();
        		
        		            	
               	/*
                var s0 = document.createElement("option");
        		$(s0).text("no value");
        		$(s0).val("no");
        		$("#id_conflict_value_p").append(s0);
                */
        		
        		
      			$.each(data, function(i, field){
            		//qui scorri l'array del piano per generare testo
            		var s = document.createElement("option");
                    var testo_select = field.name + " - " + field.nameAgent;
            		$(s).text(testo_select);
            		$(s).val(field.id);
            		$("#id_conflict_value_p").append(s);
        		});
        		
        		if (atstake_p != 0) {
        		$("#id_conflict_value_p").val(atstake_p);
        		}
        		
        		/*
                var s0 = document.createElement("option");
        		$(s0).text("no value");
        		$(s0).val("no");
        		$("#id_support_value_p").append(s0);
                */

        		        		
        		$.each(data, function(i, field){
            		//qui scorri l'array del piano per generare testo
            		var s = document.createElement("option");
            		var testo_select = field.name + " - " + field.nameAgent;
                    $(s).text(testo_select);
            		$(s).val(field.id);
            		$("#id_support_value_p").append(s);
        		});
        		
        		if (balanced_p != 0) {
        		$("#id_support_value_p").val(balanced_p);
        		}

                /*
                var s0 = document.createElement("option");
                $(s0).text("no value");
                $(s0).val("no");
                $("#id_conflict_value_e").append(s0);
                */
                
                
                $.each(data, function(i, field){
                    //qui scorri l'array del piano per generare testo
                    var s = document.createElement("option");
                    var testo_select = field.name + " - " + field.nameAgent;
                    $(s).text(testo_select);
                    $(s).val(field.id);
                    $("#id_conflict_value_e").append(s);
                });
                
                if (atstake_e != 0) {
                $("#id_conflict_value_e").val(atstake_e);
                }
                
                /*
                var s0 = document.createElement("option");
                $(s0).text("no value");
                $(s0).val("no");
                $("#id_support_value_e").append(s0);
                */

                                
                $.each(data, function(i, field){
                    //qui scorri l'array del piano per generare testo
                    var s = document.createElement("option");
                    var testo_select = field.name + " - " + field.nameAgent;
                    $(s).text(testo_select);
                    $(s).val(field.id);
                    $("#id_support_value_e").append(s);
                });
                
                if (balanced_e != 0) {
                $("#id_support_value_e").val(balanced_e);
                }
        		
      		 });
      		//} // if su agent  
      		//else {	
      		//	}
      		//fine valori	
      
       });
       
       //window.alert("carico le azioni");
      // get_actions(plan);
       }
                              
    });
});


// ******* CARICAMENTO MENU GOALS ******

// commentata 7/6/2017 per modifica gestione dei goal
// eliminato menu
/*
$(document).ready(function(){


   // $("body").load(function(){})
   $("#id_select_plan").change(function(){ 	
   	
        $.getJSON("get_goals.php", function(data, status){
        	$("#id_select_goal").empty();
        	
        	var s0 = document.createElement("option");
        	$(s0).text("Select a goal");
        	$("#id_select_goal").append(s0);
        	
            $.each(data, function(i, field){
            	//qui scorri l'array dei goal per generare descrizione
            	var s = document.createElement("option");
            	$(s).text(field.name);
            	$(s).val(field.id);
            	$("#id_select_goal").append(s);
        	})    
        });
    });
});
*/



// ***** CARICAMENTO DEL MENU ACTIONS ******

$(document).ready(function(){

   // $("body").load(function(){})
   $("#id_select_plan").change(function(){ 	
   
     	var plan = $("#id_select_plan").val();  	
       	//get_actions (plan);
       
    });
});







// ***** CARICAMENTO MENU AGENTI ******

// creazione di menu degli agenti 
// id_select_agent
// get_agents.php
// [{"id":"1","print":"amico del giaguaro","name":"Amleto"},{"id":"2","print":"la sciacquetta","name":"Ofelia"},{"id":"3","print":"il cattivo","name":"Claudio"}]

$(document).ready(function(){
   
   	$("#id_select_plan").change(function(){ 	
        $.getJSON("get_agents.php", function(data, status){
        	$("#id_select_agent").empty();
        	var s0 = document.createElement("option");
        	$(s0).text("Select an agent");
        	$("#id_select_agent").append(s0);
            $.each(data, function(i, field){
            	//qui scorri l'arr ay del piano per generare testo
            	var s = document.createElement("option");
            	$(s).text(field.name);
            	$(s).val(field.id);
            	$("#id_select_agent").append(s);
        	})    
        });
    });
});







// **** SELEZIONE DI UNA UNIT ****

// alla selezione si esegue:
//clear_pagina()
//setCurrentUnit(this.value)
//get_all(this.value,id_timeline_corrente);

function clear_pagina (){
	

	/*
	document.getElementById("id_p_viewer").reset();
	document.getElementById("id_e_viewer").reset();
	document.getElementById("form_incidents").reset();
	removeOptions(document.getElementById("precondizioni_unit"));
    removeOptions(document.getElementById("effetti_unit"));
    removeOptions(document.getElementById("precondizioni_candidate"));
    removeOptions(document.getElementById("effetti_candidati"));
    */  

    removeOptions(document.getElementById("candidate_previous"));
	removeOptions(document.getElementById("candidate_next")); 
	//removeOptions(document.getElementById("curr_incident"));
    //removeOptions(document.getElementById("cand_incident"));
    
    //aggiunte 24/4/2017
    document.getElementById('campo_unit_precedente').innerHTML = '_____';
	document.getElementById('description_unit_precedente').innerHTML = '_____';
	document.getElementById('campo_unit_seguente').innerHTML = '_____';
	document.getElementById('description_unit_seguente').innerHTML = '_____';
    document.getElementById('id_echo_annotatore').innerHTML = '_____';
    
   
	
	clear_pagina_low();    
    	
}




function clear_pagina_low (){
	//window.alert("sono in clear pagina");
	
	// cancello il piano corrente
	/*
	$("#id_select_plan").empty();
    var init =  document.createElement("option");
    $(init).text("Select a plan");
    $(init).val("");
    $("#id_select_plan").append(init);
    */
	
	
	
	
	//removeOptions(document.getElementById('cand_incident'));
	 /*
	 
	 var s1 = document.getElementById("cand_incident");
            	//var s2 = document.getElementById("cand_incident");
            	
            	var istr1 = document.createElement("option");
            	//var istr2 = document.createElement("option");
            	istr1.text = "select an action";
            	//istr2.text = "select an action";
            	s1.add(istr1);
            	//s2.add(istr2);
     */       	
    
    //document.getElementById('id_name_action_corrente').innerHTML = '____';
    //document.getElementById('id_description_action_corrente').innerHTML = '____';
    //document.getElementById('name_incident').value=""; 
    //document.getElementById('id_print_incident').value=""; 
    
    
    
    //document.getElementById('campo_unit_precedente').innerHTML = '_____';
	//document.getElementById('description_unit_precedente').innerHTML = '_____';
	//document.getElementById('campo_unit_seguente').innerHTML = '_____';
	//document.getElementById('description_unit_seguente').innerHTML = '_____';
	
	
	//AZZERO MENU AGENT E GOAL
	removeOptions(document.getElementById('id_select_agent'));
	//removeOptions(document.getElementById('id_select_goal'));
	
	var s11 = document.getElementById("id_select_agent");
	var istr11 = document.createElement("option");
	istr11.text = "Select an agent";
	s11.add(istr11);

	//var s12 = document.getElementById("id_select_goal");
	//var istr12 = document.createElement("option");
	//istr12.text = "Select a goal";
	//s12.add(istr12);

	
		
	//AZZERARE A MANO TUTTI I CAMPI 
    $("#id_name_agent_corrente").html("____");
	$("#id_description_agent_corrente").html("____");
    $("#id_print_values").html("____");
	$("#id_name_goal").html("____");
	$("#id_description_goal_corrente").html("____");
    $("#id_print_like").html("______");
    $("#id_print_dislike").html("______");
    $("#id_print_values").html("_____");
	$("#id_accomplished_yes").prop('checked', true);
	$("#id_accomplished_no").prop('checked', false);
    $("#id_pleasant_yes").prop('checked', true);
    $("#id_pleasant_no").prop('checked', false);
	

	// resetto il box di destra con le properties del piano
	
	// id_accomplished_yes/no
	// id_conflict_plan
	// id_support_plan
	// id_conflict_value
	// id_support_value
	
	
	removeOptions(document.getElementById('id_conflict_plan'));
	removeOptions(document.getElementById('id_support_plan'));
	removeOptions(document.getElementById('id_conflict_value_p'));
	removeOptions(document.getElementById('id_support_value_p'));
    removeOptions(document.getElementById('id_conflict_value_e'));
    removeOptions(document.getElementById('id_support_value_e'));
	
	/*
    var s1 = document.getElementById("id_conflict_plan");
	var istr1 = document.createElement("option");
	istr1.text = "no plan";
	s1.add(istr1);
	
	var s2 = document.getElementById("id_support_plan");
	var istr2 = document.createElement("option");
	istr2.text = "no plan";
	s2.add(istr2);
    */
	
	
    /*
    var s3 = document.getElementById("id_conflict_value_p");
	var istr3 = document.createElement("option");
	istr3.text = "no value";
	s3.add(istr3);
	
	var s4 = document.getElementById("id_support_value_p");
	var istr4 = document.createElement("option");
	istr4.text = "no value";
	s4.add(istr4);

    var s3 = document.getElementById("id_conflict_value_e");
    var istr3 = document.createElement("option");
    istr3.text = "no value";
    s3.add(istr3);
    
    var s4 = document.getElementById("id_support_value_e");
    var istr4 = document.createElement("option");
    istr4.text = "no value";
    s4.add(istr4);
    */


    
    	
}


function clear_box_plan (){

    $("#description_deplan_corrente").html("____");
    $("#id_name_deplan_corrente").html("____");
    $("#name_action").html("____");

}




// **** setta la unit corrente, la timeline e i campi nascosti ****
// **** al click sul menu delle unit

function setCurrentUnit (unit) {
	
	//window.alert("lavoro su unit: " + unit);
	
	if (document.getElementById('check').value != "no"){
		//document.getElementById('test').innerHTML = "hai selezionato: " + candidate_unit;		
		if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
		xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	//var response = xmlhttp.responseText;

            	var my_response = JSON.parse(xmlhttp.responseText);
            	document.getElementById('id_name_unit_corrente').innerHTML = my_response.name;  
            	document.getElementById('description_unit_corrente').innerHTML = my_response.description;  

                document.getElementById('id_echo_annotatore').innerHTML = my_response.annotator;

                //document.getElementById('binding_unit_corrente').innerHTML = my_response.reference;
                document.getElementById('type_unit_corrente').value = my_response.isref;
            	document.getElementById('id_timeline_corrente').value = my_response.timeline;
            	document.getElementById('id_unit_corrente').value = unit;
            	document.getElementById('check').value = "ok";  
                document.getElementById('id_binding').value = my_response.text;  

                var tipo_estratto = my_response.isref;
                //var tipo_unit_scritto = $("#type_unit_corrente").val();
                //window.alert ("tipo unit settato su " + tipo_estratto );      	
			}
        }
        xmlhttp.open("GET","get_timeline.php?unit="+unit,false);
        xmlhttp.send();	
	}        	            	
}            	


// **** dopo il click sul menu delle unit, l'handler chiama anche get_all per 
// **** riempire tutti i campi ****


function get_all (unit, timeline) {

    //window.alert("unit: " + unit);
	
	if (unit == "select a unit to edit") {location.reload();
        window.alert("passo da qui");
        $("#id_binding").val("");
        } else {
	
		if (document.getElementById('check').value != "no") {
	
		get_units(unit, timeline);
		get_candidate_units(unit,timeline);
        get_reference_units();
        get_bound_reference_units();

		//get_states(unit,timeline);
		//get_actions(unit);
		//get_units_select();
		}
	}
}

// questa funzione costruisce il menu delle reference units candidate
function get_reference_units (){


    $.getJSON("get_reference_units.php", function(data, status){


        $("#id_bind_reference_unit").empty();
            var init =  document.createElement("option");
            $(init).text("Select a reference unit");
            $(init).val("");
            $("#id_bind_reference_unit").append(init);
            
            $.each(data, function(i, field){
                //qui scorri l'array del piano per generare testo
                var s = document.createElement("option");
                $(s).text(field.print);
                $(s).val(field.id);
                $("#id_bind_reference_unit").append(s);
            }) 

    });
    
}

// questa funzione costruisce il menu delle reference units che sono già bound
/*
le voci di menu possono avere 3 valori: 
- "" significa che l'utente non ha scelto quando poteva e va bloccato
- "first"
- "last"
- id di una unit



*/

function get_bound_reference_units (){

    var unit = $("#id_select_unit").val();
    $.getJSON("get_timeline.php?unit="+unit, function(data, status){

        var bound_units = data.reference;
        $("#id_order_reference_unit").empty();
            var init =  document.createElement("option");
            
            
            if (bound_units.length == 0) {$(init).val("first"); $(init).text("Place as first");} 

             else {$(init).val(""); $(init).text("Place before")}
            
            $("#id_order_reference_unit").append(init);
            
            $.each(bound_units, function(i, field){
                //qui scorri l'array del piano per generare testo
                var s = document.createElement("option");
                $(s).text(field.id_ref);
                $(s).val(field.ref_unit);
                $("#id_order_reference_unit").append(s);
            }) 
            /*
            if (bound_units == "") {
                var first =  document.createElement("option");
                $(first).text("Place as first");
                $(first).val("");
                $("#id_order_reference_unit").append(first);

            }
            */
            if (bound_units.length > 0) {
                var last =  document.createElement("option");
                $(last).text("Place as last");
                $(last).val("last");
                $("#id_order_reference_unit").append(last);
            }

        $("#id_bound_units").empty();
        $.each(bound_units, function(i, field){
                //qui scorri l'array del piano per generare testo
                var s = document.createElement("option");
                $(s).text(field.id_ref);
                $(s).val(field.ref_unit);
                $("#id_bound_units").append(s);
        }) 


    });
    
}




// fa il binding di una reference unit a una unit nella posizione selezionata


$(document).ready(function(){
    $("#id_bind_reference").click(function(){
        
        var unit = $("#id_select_unit").val();
        var bind_unit = $("#id_bind_reference_unit").val();
        var before = $("#id_order_reference_unit").val();
        //window.alert (": " + unit + "; " + "bind_unit: " + bind_unit);
        
        $.getJSON("get_timeline.php?unit="+unit, function(data, status){

            var typeUnit = data.isref;


            if (typeUnit == "annotation"){


                //controllo che sia settato unit e bind unit non sia a ""
                if ((unit != "select a unit to edit") && (bind_unit != "") && (before != "")) {

                //ROSSANA: settato dal menu apposito
                


                $.post("bind_unit.php", { unit:unit, bind:bind_unit, order:before} ,function(data){
                        
                        
                        //seleziono la unit a cui ho legato quella corrente nel menu delle reference unit

                        $('#id_bind_reference_unit').prop('selectedIndex',0);
                        //document.getElementById('binding_unit_corrente').innerHTML = data;
                        get_bound_reference_units();

                        });
                }  else {window.alert ("Please select a unit, a reference unit and a position");}

            } else {window.alert("only annotation units can be bound to reference units");}

        });    

    });

})




$(document).ready(function(){
    $("#id_unbind_reference").click(function(){
        
        var unit = $("#id_select_unit").val();
        // bound is a list
        var bound = $("#id_bound_units").val();
        //window.alert ("unbind_unit: " + bound);
    
        if ((unit != "select a unit to edit") && (bound != null)){

            
            $.post("unbind_unit.php", { unit:unit, bound:bound} ,function(data){
                
                //$('#id_bind_reference_unit').prop('selectedIndex',0);
                //document.getElementById('binding_unit_corrente').innerHTML = data;
                get_bound_reference_units();

                });
        }   else {window.alert ("Please select a unit and a reference unit to unbind");}    

    });

})



function get_units(unit, timeline) {
	
		if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	var my_response = JSON.parse(xmlhttp.responseText);
            	var pre = my_response.pre;
            	var eff = my_response.eff;
            	var predescription = my_response.pre_description;
            	var effdescription = my_response.eff_description;
                document.getElementById('campo_unit_precedente').innerHTML = pre;
                document.getElementById('campo_unit_seguente').innerHTML = eff;
                document.getElementById('description_unit_precedente').innerHTML = predescription;
                document.getElementById('description_unit_seguente').innerHTML = effdescription;

            }
        }
        xmlhttp.open("GET","get_units.php?unit="+unit+"&"+"timeline="+timeline,false);
        xmlhttp.send();	
}

//[{"id":"1","print":"Unit1"},{"id":"2","print":"Unit2"},{"id":"3","print":"Unit3"},{"id":"4","print":"Unit4"}]
function get_candidate_units(unit,timeline) {	
		if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	//var my_string = "<select>";
            	var response = xmlhttp.responseText;
            	//window.alert (response);
            	var my_response = JSON.parse(xmlhttp.responseText);
            	
            	        	
            	removeOptions(document.getElementById("candidate_previous"));
				removeOptions(document.getElementById("candidate_next"));
					            	
            	var s1 = document.getElementById("candidate_previous");
            	var s2 = document.getElementById("candidate_next");
            	var istr1 = document.createElement("option"); 
    			var istr2 = document.createElement("option");
    			istr1.text = "select previous unit";
    			istr2.text = "select next unit";
    			s1.add(istr1);
    			s2.add(istr2);
            	
            	  	
            	my_response.forEach(function(entry) {
    				var id = entry.id;
    				var print = entry.print;
    				var option1 = document.createElement("option");
    				var option2 = document.createElement("option");
                    //aggiunto per stampare la [R] in previous e next
                    var reference = entry.is_reference;
                    if (reference == "reference") {print = print + " [R]";}
					option1.text = print;
					option1.value = id;
					option2.text = print;
					option2.value = id;
					s1.add(option1);
					s2.add(option2);
				});
				//window.alert ("secondo passaggio");
            }
        }
        xmlhttp.open("GET","get_units_candidate.php?unit="+unit+"&"+"timeline="+timeline,false);
        xmlhttp.send();	
}

function get_states(unit, timeline) {	
	
		//window.alert("get_states.php?unit="+unit+"&"+"timeline="+timeline);
	
		if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	
            	var response = xmlhttp.responseText;
            	//document.getElementById('test').innerHTML = response;   
            	//window.alert (response);
            	
            	var my_response = JSON.parse(xmlhttp.responseText);        	
            	var s1 = document.getElementById("precondizioni_unit");
            	var s2 = document.getElementById("effetti_unit");
            	var s3 = document.getElementById("precondizioni_candidate");
            	var s4 = document.getElementById("effetti_candidati");       
            	
            	//inizializzazione con option di istruzione
            	var istr1 = document.createElement("option"); 
    			var istr2 = document.createElement("option"); 
    			var istr3 = document.createElement("option"); 
    			var istr4 = document.createElement("option"); 
    			istr1.text = "select a state";
    			istr2.text = "select a state";
    			istr3.text = "select a state";
    			istr4.text = "select a state";
    			s1.add(istr1);
    			s2.add(istr2);
    			s3.add(istr3);
    			s4.add(istr4);
            	
            	
            	         
                var list_pre = my_response.preconditions;
                var list_eff = my_response.effects; 
                var list_cand_pre = my_response.candidate_preconditions;
                var list_cand_eff = my_response.candidate_effects;
                
                list_pre.forEach(function(entry) {
    				var id = entry.id;
    				var printState = entry.name;
    				var option = document.createElement("option");
					option.text = printState;
					option.value = id;
					s1.add(option);
					});
				list_eff.forEach(function(entry) {
    				var id = entry.id;
    				var printState = entry.name;
    				var option = document.createElement("option");
					option.text = printState;
					option.value = id;
					s2.add(option);
					});	
				list_cand_pre.forEach(function(entry) {
    				var id = entry.id;
    				var printState = entry.name;
    				var option = document.createElement("option");
					option.text = printState;
					option.value = id;
					s3.add(option);
					});	
				list_cand_eff.forEach(function(entry) {
    				var id = entry.id;
    				var printState = entry.name;
    				var option = document.createElement("option");
					option.text = printState;
					option.value = id;
					s4.add(option);
					});	
				//window.alert ("get states 2");	
							
              	}
        }
        xmlhttp.open("GET","get_states_unit.php?unit="+unit+"&"+"timeline="+timeline,false);
        xmlhttp.send();	
}



function get_actions_unit (unit) {
		//document.getElementById('test').innerHTML = "hai selezionato: " + candidate_unit;		
		//window.alert ("cerco le actions della unit " + unit);
		
		if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
		xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	//var response = xmlhttp.responseText;
            	//document.getElementById('test').innerHTML = response;           	
            	
            	var my_response = JSON.parse(xmlhttp.responseText);
            	var a = my_response.actions;
            	var ca = my_response.candidate;
            	
            	removeOptions(document.getElementById("curr_incident"));
            	//removeOptions(document.getElementById("cand_incident"));
            	
            	var s1 = document.getElementById("curr_incident");
            	//var s2 = document.getElementById("cand_incident");
            	
            	var istr1 = document.createElement("option");
            	//var istr2 = document.createElement("option");
            	istr1.text = "select an action";
            	//istr2.text = "select an action";
            	s1.add(istr1);
            	//s2.add(istr2);
            	
            	a.forEach(function(entry) {
    				var id = entry.id;
    				var printAction = entry.print;
    				var option = document.createElement("option");
					option.text = printAction;
					option.value = id;
					s1.add(option);
					});	
					
            	//ca.forEach(function(entry) {
    			//	var id = entry.id;
    			//	var printAction = entry.print;
    			//	var option = document.createElement("option");
				//	option.text = printAction;
				//	option.value = id;
				//	s2.add(option);
				//	});
				
	 		}
        }
        xmlhttp.open("GET","get_actions_unit.php?unit="+unit,false);
        xmlhttp.send();		
}	



// **** RECUPERO LE AZIONI DEL PIANO ****



function get_actions (unit) {
		//document.getElementById('test').innerHTML = "hai selezionato: " + candidate_unit;	
		//window.alert("cerco le azioni del piano " + unit);	
	if (unit != "")	{
		if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
		xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	//var response = xmlhttp.responseText;
            	//document.getElementById('test').innerHTML = response;           	
            	
            	var my_response = JSON.parse(xmlhttp.responseText);
            	var a = my_response.actions;
            	var ca = my_response.candidate;
            	
            	//removeOptions(document.getElementById("curr_incident"));
            	removeOptions(document.getElementById("cand_incident"));
            	
            	//var s1 = document.getElementById("curr_incident");
            	var s2 = document.getElementById("cand_incident");
            	
            	//var istr1 = document.createElement("option");
            	var istr2 = document.createElement("option");
            	//istr1.text = "select an action";
            	istr2.text = "select an action";
            	//s1.add(istr1);
            	s2.add(istr2);
            	
            	/*
            	a.forEach(function(entry) {
    				var id = entry.id;
    				var printAction = entry.print;
    				var option = document.createElement("option");
					option.text = printAction;
					option.value = id;
					s1.add(option);
					});	
				*/	
					
            	ca.forEach(function(entry) {
    				var id = entry.id;
    				var printAction = entry.print;
    				var option = document.createElement("option");
					option.text = printAction;
					option.value = id;
					s2.add(option);
					});
				
	 		}
        }
        xmlhttp.open("GET","get_actions.php?plan="+unit,false);
        xmlhttp.send();	
	}   	
}





// **** GESTIONE UNIT PRECEDENTE E SEGUENTE ****

function changePair (candidate_unit, unit, timeline, flag){
		//document.getElementById('test').innerHTML = "hai selezionato: " + candidate_unit;		
		if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
		xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	
            	var response = xmlhttp.responseText; 
            	
            	var my_response = JSON.parse(xmlhttp.responseText);
            	var id = my_response.id;
            	var print = my_response.print;      
            	var descr = my_response.description;        
                
                if (flag == 'pre'){
                document.getElementById('campo_unit_precedente').innerHTML = print;
                document.getElementById('id_unit_precedente').value = id;
                document.getElementById('description_unit_precedente').innerHTML = descr;
                //window.alert (response);
                
                } else {
                	document.getElementById('campo_unit_seguente').innerHTML = print;
                	document.getElementById('id_unit_seguente').value = id;
                	document.getElementById('description_unit_seguente').innerHTML = descr;
                	}
                //document.getElementById('test').innerHTML = response;
                
                
            }
        }
        xmlhttp.open("GET","change_pair.php?candidate_unit="+candidate_unit+"&"+"unit="+unit+"&"+"timeline="+timeline+"&"+"flag="+flag,false);
        xmlhttp.send();	
	
}



// **** GESTIONE AZIONI ****


function getInfoAction (action){
		//document.getElementById('test').innerHTML = "hai selezionato: " + candidate_unit;		
		if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
		xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	var response = xmlhttp.responseText;
            	//document.getElementById('test').innerHTML = response; 
            	var my_response = JSON.parse(xmlhttp.responseText);
                
                $("#name_action").html(my_response.name);
                //document.getElementById('test').innerHTML = response;
                //document.getElementById("id_name_action_corrente").innerHTML = my_response.name; 
                //document.getElementById("id_description_action_corrente").innerHTML = my_response.print; 
                //document.getElementById("set_state_p_"+flag).innerHTML = ... ;
                //document.getElementById("id_print_incident").value = my_response.print;             
                //document.getElementById('set_incident').innerHTML  = my_response.nameunit;
                //document.getElementById('name_incident').value  = my_response.name;
            }
        }
        xmlhttp.open("GET","get_action_features.php?action="+action,false);
        xmlhttp.send();	
	
}


// ************************************
// ****** FUNZIONI DI AGGIUNTA ********
// ************************************


// **** aggiunta di un agente al piano ******



$(document).ready(function(){
    $("#id_button_add_agent").click(function(){    	
      	if ($("#id_select_plan").val() != "" && $("#id_select_agent").val() != "Select an agent" ) {
    		//window.alert("add_agent2plan.php?agent="+$("#id_select_agent").val() + "&plan="+$("#id_select_plan").val());
            $.get("add_agent2plan.php?agent="+$("#id_select_agent").val() + "&plan="+$("#id_select_plan").val(), function(data, status){                             	   		
            	$.getJSON("get_agent_info.php?agent="+$("#id_select_agent").val(), function(data, status){
	
					 $("#id_description_agent_corrente").html(data.print);
            		 $("#id_name_agent_corrente").html(data.name);
            		 $("#id_print_values").html(data.values);
                     //id_print_like id_print_dislike
                     $("#id_print_like").html(data.liking);
                     $("#id_print_dislike").html(data.disliking);
                     if (data.pleasant == 1) {
                        //window.alert("passo di qui");
                    $("#id_pleasant_yes").prop('checked', true);
                    $("#id_pleasant_no").prop('checked', false);
                    } else 
                        { 
                        $("#id_pleasant_yes").prop('checked', false);
                        $("#id_pleasant_no").prop('checked', true); 
                        }

					 var agent = $("#id_select_agent").val();
            		 
            		 //ri-creare menu values
                     /*
            		 $.getJSON("get_values_agent.php?agent="+agent, function(data, status){
      			
      					$("#id_conflict_value").empty();
        				$("#id_support_value").empty();
        		            	
               			var s0 = document.createElement("option");
        				$(s0).text("no value");
        				$(s0).val("no");
        				$("#id_conflict_value").append(s0);
        		
      					$.each(data, function(i, field){
            				var s = document.createElement("option");
            				$(s).text(field.name);
            				$(s).val(field.id);
            				$("#id_conflict_value").append(s);
        				});
        		
        				var s0 = document.createElement("option");
        				$(s0).text("no value");
        				$(s0).val("no");
        				$("#id_support_value").append(s0);

        		        		
        				$.each(data, function(i, field){
            				var s = document.createElement("option");
            				$(s).text(field.name);
            				$(s).val(field.id);
            				$("#id_support_value").append(s);
        				});
      		 		});	
                    */
      		 		
      		 		$("#id_select_agent").prop('selectedIndex',0);	
      		 		
            	});            	
        });  
    	} else {window.alert ("Please select an agent and a plan");};
    });
});



// **** AGGIUNTA DEL GOAL ****

// id_goal_add
// ADD GOAL

// commentata 7/6/2017 per eliminazione menu goals
// confluita in create_goal.php
/*
$(document).ready(function(){
    $("#id_goal_add").click(function(){
    	
      if ($("#id_select_plan").val() != "" && $("#id_select_goal").val() != "Select a goal" ) {
    	
            $.get("add_goal2plan.php?goal="+$("#id_select_goal").val() + "&plan="+$("#id_select_plan").val(), function(data, status){   
            	
            	$.getJSON("get_goal_info.php?goal="+$("#id_select_goal").val(), function(data, status){
            		$("#id_description_goal_corrente").html(data.print);
            		$("#id_name_goal").html(data.name);
            		$("#id_select_goal").prop('selectedIndex',0);
            		//$("#id_select_goal").val(goal);
        		});        		 
            	    	
            });
        
        
        
      } else {window.alert("please select a plan and a goal")}
    });
});
*/


// ***** AGGIUNTA DI UN PIANO *******

/*
aggiungi il piano alla unit corrente
aggiorna le due liste: piani della unit + candidati
*/


$(document).ready(function(){
    $("#id_button_addplan").click(function(){
 
        var plan = $("#id_cand_subplan").val();
        var unit = $("#id_select_unit").val();
        var unit_type = $("#type_unit_corrente").val();   

        //aggiungere il controllo sulla prima voce del piano

        $.get("map_plan_unit.php?plan="+plan+"&unit="+unit, function(data, status){
                            
            $.getJSON("get_unit_plans.php?unit="+unit, function(data, status){
            $("#id_select_plan").empty();
            var init =  document.createElement("option");
            $(init).text("Select a plan");
            $(init).val("");
            $("#id_select_plan").append(init);
            
            $.each(data, function(i, field){
                //qui scorri l'array del piano per generare testo
                var s = document.createElement("option");
                $(s).text(field.name);
                $(s).val(field.id);
                $("#id_select_plan").append(s);
            }) 
                
            // settare la select sul piano appena creato                        
            $('#id_select_plan').prop('selectedIndex',0);
            $("#id_select_agent").prop('selectedIndex',0);
            $("#id_select_goal").prop('selectedIndex',0);
                
            //AZZERARE A MANO TUTTI I CAMPI 
            $("#id_name_agent_corrente").html("____");
            $("#id_description_agent_corrente").html("____");
            $("#id_name_goal").html("____");
            $("#id_description_goal_corrente").html("____");
                        
            document.getElementById('id_create_deplan').value = 'type name'; 
            document.getElementById('print_create_deplan').value = 'type description';
            document.getElementById('id_name_new_action').value = 'type description';

            clear_pagina_low();
            clear_box_plan();  

            //ricreo il campo dei candidate plans
            var plan_type = "Base"; 
            //"Rec" | "Base"
            if (unit_type == "annotation") {plan_type="Rec";}
            get_candidate_plans (unit,plan_type);


            });   // fine creazione menu e pulizia pagina
        }); // fine map_unit_plans
    });
});    



$(document).ready(function(){
    $("#id_button_detach_plan").click(function(){
 
        //var plan = $("#id_cand_subplan").val();
        var plan = $("#id_select_plan").val();
        var unit = $("#id_select_unit").val();
        var unit_type = $("#type_unit_corrente").val();   

        //aggiungere il controllo sulla prima voce del piano

        $.get("unmap_plan_unit.php?plan="+plan+"&unit="+unit, function(data, status){
                            
            $.getJSON("get_unit_plans.php?unit="+unit, function(data, status){
            $("#id_select_plan").empty();
            var init =  document.createElement("option");
            $(init).text("Select a plan");
            $(init).val("");
            $("#id_select_plan").append(init);
            
            $.each(data, function(i, field){
                //qui scorri l'array del piano per generare testo
                var s = document.createElement("option");
                $(s).text(field.name);
                $(s).val(field.id);
                $("#id_select_plan").append(s);
            }) 
                
            // settare la select sul piano appena creato                        
            $('#id_select_plan').prop('selectedIndex',0);
            $("#id_select_agent").prop('selectedIndex',0);
            $("#id_select_goal").prop('selectedIndex',0);
                
            //AZZERARE A MANO TUTTI I CAMPI 
            $("#id_name_agent_corrente").html("____");
            $("#id_description_agent_corrente").html("____");
            $("#id_name_goal").html("____");
            $("#id_description_goal_corrente").html("____");
                        
            document.getElementById('id_create_deplan').value = 'type name'; 
            document.getElementById('print_create_deplan').value = 'type description';
            document.getElementById('id_name_new_action').value = 'type description';

            clear_pagina_low();
            clear_box_plan();  

            //ricreo il campo dei candidate plans
            var plan_type = "Base"; 
            //"Rec" | "Base"
            if (unit_type == "annotation") {plan_type="Rec";}
            get_candidate_plans (unit,plan_type);


            });   // fine creazione menu e pulizia pagina
        }); // fine map_unit_plans
    });
});    

// ***** CREAZIONE DI UN PIANO ******


// creazione di un piano
// id_button_create_deplan
// id_create_plan id_print_create_plan
// id_create_deplan
// print_create_deplan
// create_plan.php?name=Piano+prova&print=guarda+che+bello&type=Base

// il piano creato va subito associato alla unit correte id_unit_select

// DA FARE 1/3/2018!!!!
// deve essere creato con il flag giusto
// o meglio del tipo giusto
// disabilitare il campo action se la unit è una macrounit (annotation)

$(document).ready(function(){
    $("#id_button_create_deplan").click(function(){
    	
     if 
        //(($("#id_create_deplan").val() != "type name") && ($("#id_name_new_action").val() != "type description")) 
        ($("#id_create_deplan").val() != "type name")
        {	
     	
     	var action = $("#id_name_new_action").val();

        if (action == "type description") {action = "";}
     	
     	$.get("create_action_id.php?action="+action, function(data,success){
     		
     		var id_action = parseInt(data);

            var plan_description = $("#print_create_deplan").val();

            if (plan_description == "type description") {plan_description = "";} 
            
            //modifica 15/02/2018: aggiunto
            
            var unit_type = $("#type_unit_corrente").val();
            var plan_type = "Base"; 
            //"Rec" | "Base"

            if (unit_type == "annotation") {plan_type="Rec";}

            

    	  //window.alert("create_plan.php?name="+$("#id_create_deplan").val()+"&print="+$("#print_create_deplan").val()+"&type=Base" + "&action=" + id_action);
		  
          // modifica 15/02/2018
          $.get("create_plan.php?name="+$("#id_create_deplan").val()+"&print="+plan_description+"&type="+plan_type+"&accomplished="+$("#id_accomplished").val() + "&action=" + id_action, function(data, success){
    
  				var unit = $("#id_select_unit").val();
    			var nuovo_id = parseInt(data);
 
				$.getJSON("get_plan.php?plan="+nuovo_id, function(data, status){
				// visualizzo i dati
	
        		// mettere name e description al loro posto
        		$("#id_name_absplan_corrente").html(data.name);
				$("#description_absplan_corrente").html(data.print);
				
				});
				
				$.get("map_plan_unit.php?plan="+nuovo_id+"&unit="+unit, function(data, status){
            	
        		

        		$.getJSON("get_unit_plans.php?unit="+unit, function(data, status){
   			
        		$("#id_select_plan").empty();
        		
        		var init =  document.createElement("option");
        		$(init).text("Select a plan");
            	$(init).val("");
            	$("#id_select_plan").append(init);
				var my_index = 0;

            		$.each(data, function(i, field){
            		//qui scorri l'array del piano per generare testo
            		//window.alert("passo di qui");

            		var s = document.createElement("option");
            		$(s).text(field.name);
            		$(s).val(field.id);
            		//window.alert (field.id);
            		$("#id_select_plan").append(s);
            		my_index++;
            		
            			if (field.id == nuovo_id) {nuovo_index = my_index;} 
            				            		
        			})
        		
        		// settare la select sul piano appena creato          		 	
        		//$('#id_select_plan').change();	
        		//$('#id_select_plan').val(nuovo_id);  		
        		$('#id_select_plan').prop('selectedIndex',0);
        		$("#id_select_agent").prop('selectedIndex',0);
        		$("#id_select_goal").prop('selectedIndex',0);
         	    
        	    //AZZERARE A MANO TUTTI I CAMPI 
        	    $("#id_name_agent_corrente").html("____");
				$("#id_description_agent_corrente").html("____");
				$("#id_name_goal").html("____");
				$("#id_description_goal_corrente").html("____");
        	       	    
        	    document.getElementById('id_create_deplan').value = 'type name'; 
        	    document.getElementById('print_create_deplan').value = 'type description';
        	    document.getElementById('id_name_new_action').value = 'type description';

                clear_pagina_low();
                clear_box_plan();

        	    
        	    //azzera il box del subplan
        	    /*
            	$("#id_id_name").val("")	
				$("#id_print_incident").val("");
				$("#id_agent").html("");
				$("#id_goal").html("");
				$("#id_mapping_init").html("");
				$("#id_mapping_end").html("");	
				$("#id_flag_accomplished").html("");
				$("#cur_subplan").html("");
				*/        	    
        	    //document.getElementById('name_incident').value=""; 
        	    //document.getElementById('id_print_incident').value=""; 
     						    
        		});   // fine creazione menu e pulizia pagina
            }); // fine map_unit_plans
 		  });	
 		  // chiudo creazione di azione	  		
     	}); 
      }	else {window.alert ("Please make sure you have inserted \na plan name and an action name");}
    }); 
});











// **** AGGIUNTA AZIONE AL PIANO ****

$(document).ready(function(){
    $("#id_button_addaction").click(function(){   	
    	// aggiungo il piano
    	
    	var father_id = $("#id_select_plan").val();
		var child_id = $("#cand_incident").val();
		
		if (father_id != ""){
		//window.alert("aggiungo azione " + child_id + " al piano " + father_id);
		//var reference_id = $("#id_dopo").val();
		
			addAction(child_id, father_id);
			//get_actions(father_id);
			getInfoAction (child_id);
			
		} else {window.alert("please select a plan");}
			
	});
});	







function addAction (action, plan){
		//if (document.getElementById('check').value != "no"){
		//document.getElementById('test').innerHTML = "hai selezionato: " + unit;	
		//window.alert("");	
		if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
		xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	var response = xmlhttp.responseText;
            	//document.getElementById('test').innerHTML = response; 
            	           	
            	//removeOptions(document.getElementById("curr_incident"));
            	removeOptions(document.getElementById("cand_incident"));
            	get_actions(plan);
                                    
            }
        }
        //add_action2plan.php?action=5&plan=5
        xmlhttp.open("GET","add_action2plan.php?plan="+plan+"&"+"action="+action,false);
        //xmlhttp.open("GET","add_action.php?unit="+unit+"&"+"action="+action+"&"+"print="+print,false);
        xmlhttp.send();	
	//}    
	
}



// ****** MODIFICA DEL VALORE ACCOMPLISHED ****


$(document).ready(function(){
    $("#id_accomplished_yes").click(function(){
    	var plan = $("#id_select_plan").val();
    	
    	if ($("#id_select_plan").val() != ""){
    	
    	//window.alert("accomplished settato a  " +  acc);
    	$.getJSON("set_plan_features.php?accomplished=1&plan="+plan, function(data, status){	
    			
    		//window.alert("accomplished settato a  " +  acc);
    	
    	});
    	
    	} 
    });
})

$(document).ready(function(){
    $("#id_accomplished_no").click(function(){
    	var plan = $("#id_select_plan").val();
    	
    	if (plan != ""){

    	
    	//window.alert("accomplished settato a  " +  acc);
    	$.getJSON("set_plan_features.php?accomplished=0&plan="+plan, function(data, status){	
    			
    		//window.alert("accomplished settato a  " +  acc);
    	
    	});
    	
    	}
    });
})

// ***** MODIFICA PLEASANT DI AGENT ******

// id_pleasant_yes 

$(document).ready(function(){
    $("#id_pleasant_yes").click(function(){
        
        var plan = $("#id_select_plan").val();
        
        $.getJSON("get_plan.php?plan="+plan, function(data, status){       
            
            var current_agent = data.agent;
        
            if ((current_agent != null) && (new_agent_name != "")){  
        
                //window.alert("accomplished settato a  " +  acc);
                $.getJSON("change_agent_features.php?agent="+current_agent+"&new_pleasant="+1, function(data, status){    
                
                //window.alert("accomplished settato a  " +  acc);
        
                });
        
            } 
        });

    });
})


//id_pleasant_no
$(document).ready(function(){
    $("#id_pleasant_no").click(function(){
        
        var plan = $("#id_select_plan").val();
        
        $.getJSON("get_plan.php?plan="+plan, function(data, status){       
            
            var current_agent = data.agent;
        
            if ((current_agent != null) && (new_agent_name != "")){  
        
                //window.alert("accomplished settato a  " +  acc);
                $.getJSON("change_agent_features.php?agent="+current_agent+"&new_pleasant="+0, function(data, status){    
                
                //window.alert("accomplished settato a  " +  acc);
        
                });
        
            } 
        });

    });
})


// *** MODIFICA DEL CONFLICT SET ****

//id_conflict_plan
//id_support_plan

$(document).ready(function(){
    $("#id_conflict_plan").change(function(){
    	var plan = $("#id_select_plan").val();  
    	
    	if (plan != ""){
    	
    	var confl = $("#id_conflict_plan").val();
        //window.alert("Plan in concflict vale: " + confl);
    	if (confl == "no") {confl = 0;}
    	   //window.alert("setto il conflict set del piano  " +  plan + " a " + confl);
    	   $.post("set_plan_interactions.php", {confl:confl, plan:plan, relation: "conflict_p"}, function(data, status){ 

            //$.getJSON("set_plan_features.php?confl="+ confl +"&plan="+plan, function(data, status){		
    	
    	});
    	}
    });
})




// *** MODIFICA DEL SUPPORT SET ****

//id_conflict_plan
//id_support_plan

$(document).ready(function(){
    $("#id_support_plan").change(function(){
    	var plan = $("#id_select_plan").val();
    	
    	if (plan != ""){
    	
    	var supp = $("#id_support_plan").val();
    	if (supp == "no") {supp = 0;}
    	//window.alert("setto il conflict set del piano  " +  plan + " a " + confl);
    	$.post("set_plan_interactions.php", {confl:supp, plan:plan, relation: "support_p"}, function(data, status){	
    			
    		
    	
    	});
    	}
    });
})

// *********** MOFIDICA VALUES *******

// API stake / bal

// id_conflict_value
// id_support_value


//$.post("bind_text.php", { text:text, unit:unit, start:start, end:end },function(data){


$(document).ready(function(){
    $("#id_conflict_value_p").change(function(){
        var plan = $("#id_select_plan").val();
        
        if (plan != ""){

        var stake = $("#id_conflict_value_p").val();
        if (stake == "no") {stake = 0;}
        //window.alert("setto il conflict set del piano  " +  plan + " a " + confl);
        
        //$.getJSON("set_plan_features.php?stake_p="+ stake +"&plan="+plan, function(data, status){   
        $.post("set_plan_values.php", { values:stake, plan:plan, relation: "conflict_p"},function(data){  

        
        });
        
        }
    });
})

/*
$(document).ready(function(){
    $("#id_conflict_value_p").change(function(){
    	var plan = $("#id_select_plan").val();
    	
    	if (plan != ""){

    	var stake = $("#id_conflict_value_p").val();
    	if (stake == "no") {stake = 0;}
    	//window.alert("setto il conflict set del piano  " +  plan + " a " + confl);
    	$.getJSON("set_plan_features.php?stake_p="+ stake +"&plan="+plan, function(data, status){	
    			  		
    	
    	});
    	
    	}
    });
})
*/


$(document).ready(function(){
    $("#id_support_value_p").change(function(){
        var plan = $("#id_select_plan").val();
        
        if (plan != ""){

        var bal = $("#id_support_value_p").val();
        if (bal == "no") {bal = 0;}
        //window.alert("setto il conflict set del piano  " +  plan + " a " + confl);
        
        //$.getJSON("set_plan_features.php?stake_p="+ stake +"&plan="+plan, function(data, status){   
        $.post("set_plan_values.php", { values:bal, plan:plan, relation: "support_p"},function(data){  

        
        });
        
        }
    });
})

/*
$(document).ready(function(){
    $("#id_support_value_p").change(function(){
    	var plan = $("#id_select_plan").val();
    	
    	if (plan != ""){
    	
    	var bal = $("#id_support_value_p").val();
    	if (bal == "no") {bal = 0;}
    	//window.alert("setto il conflict set del piano  " +  plan + " a " + confl);
    	$.getJSON("set_plan_features.php?bal_p="+ bal +"&plan="+plan, function(data, status){	
    			  		
    	
    	});
    	
    	}
    	
    });
})
*/


/*

$(document).ready(function(){
    $("#id_conflict_value_e").change(function(){
        var plan = $("#id_select_plan").val();
        
        if (plan != ""){

        var stake = $("#id_conflict_value_e").val();
        if (stake == "no") {stake = 0;}
     
        //window.alert("setto il conflict set del piano  " +  plan + " a " + confl);
        $.getJSON("set_plan_features.php?stake_e="+ stake +"&plan="+plan, function(data, status){ 
                        
        
        });
        
        }
    });
})

$(document).ready(function(){
    $("#id_support_value_e").change(function(){
        var plan = $("#id_select_plan").val();
        
        if (plan != ""){
        
        var bal = $("#id_support_value_e").val();
        if (bal == "no") {bal = 0;}
        //window.alert("setto il conflict set del piano  " +  plan + " a " + confl);
        $.getJSON("set_plan_features.php?bal_e="+ bal +"&plan="+plan, function(data, status){ 
                        
        
        });
        
        }
        
    });
})

*/

$(document).ready(function(){
    $("#id_conflict_value_e").change(function(){
        var plan = $("#id_select_plan").val();
        
        if (plan != ""){

        var stake = $("#id_conflict_value_e").val();
        if (stake == "no") {stake = 0;}
        //window.alert("setto il conflict set del piano  " +  plan + " a " + confl);
        
        //$.getJSON("set_plan_features.php?stake_p="+ stake +"&plan="+plan, function(data, status){   
        $.post("set_plan_values.php", { values:stake, plan:plan, relation: "conflict_e"},function(data){  

        
        });
        
        }
    });
})

$(document).ready(function(){
    $("#id_support_value_e").change(function(){
        var plan = $("#id_select_plan").val();
        
        if (plan != ""){

        var bal = $("#id_support_value_e").val();
        if (bal == "no") {bal = 0;}
        //window.alert("setto il conflict set del piano  " +  plan + " a " + confl);
        
        //$.getJSON("set_plan_features.php?stake_p="+ stake +"&plan="+plan, function(data, status){   
        $.post("set_plan_values.php", { values:bal, plan:plan, relation: "support_e"},function(data){  

        
        });
        
        }
    });
})


// **** MODIFICA VALUES UNIT *****

// menu per la selezione 
// id_value_atStake_before
// id_value_inBalance_before
// id_value_atStake_after
// id_value_inBalance_after

// per ogni pulsante:
// 1. leggi il value 
// 2. chiama add_VAS2Unit.php
// 3. se 2 ok, setta il value 

// unit timeline  
// flag (value: balanced OR at_stake)

$(document).ready(function(){
    $("#id_value_atStake_before").change(function(){
        var value = $("#id_value_atStake_before").val();
        var unit = $("#id_select_unit").val();
        var timeline_corrente = $("#id_timeline_corrente").val();

        //window.alert ("add_VAS2Unit.php?unit="+unit+"&timeline="+timeline_corrente+"&flag=pre&at_stake="+value);

        $.post("add_VAS2Unit_multiple.php", { unit:unit, timeline:timeline_corrente, flag:"pre", at_stake:value}, function(data, status){      
        //$.getJSON("add_VAS2Unit.php?unit="+unit+"&timeline="+timeline_corrente+"&flag=pre&at_stake="+value, function(data, status){            
              
              //Cosa fa questa funzione?
              refreshUnitValues();          
        });
    });    
})

$(document).ready(function(){
    $("#id_value_inBalance_before").change(function(){
        var value = $("#id_value_inBalance_before").val();
        var unit = $("#id_select_unit").val();
        var timeline_corrente = $("#id_timeline_corrente").val();

        //window.alert ("add_VAS2Unit.php?unit="+unit+"&timeline="+timeline_corrente+"&flag=pre&at_stake="+value);
        //$.getJSON("add_VAS2Unit.php?unit="+unit+"&timeline="+timeline_corrente+"&flag=pre&balanced="+value, function(data, status){   
        $.post("add_VAS2Unit_multiple.php", { unit:unit, timeline:timeline_corrente, flag:"pre", balanced:value}, function(data, status){           
              refreshUnitValues();          
        });
    });    
})

$(document).ready(function(){
    $("#id_value_atStake_after").change(function(){
        var value = $("#id_value_atStake_after").val();
        var unit = $("#id_select_unit").val();
        var timeline_corrente = $("#id_timeline_corrente").val();

        //window.alert ("add_VAS2Unit.php?unit="+unit+"&timeline="+timeline_corrente+"&flag=eff&at_stake="+value);
        //$.getJSON("add_VAS2Unit.php?unit="+unit+"&timeline="+timeline_corrente+"&flag=eff&at_stake="+value, function(data, status){  
         $.post("add_VAS2Unit_multiple.php", { unit:unit, timeline:timeline_corrente, flag:"eff", at_stake:value}, function(data, status){         
              refreshUnitValues();          
        });
    });    
})

$(document).ready(function(){
    $("#id_value_inBalance_after").change(function(){
        var value = $("#id_value_inBalance_after").val();
        var unit = $("#id_select_unit").val();
        var timeline_corrente = $("#id_timeline_corrente").val();

        //window.alert ("add_VAS2Unit.php?unit="+unit+"&timeline="+timeline_corrente+"&flag=eff&at_stake="+value);
        //$.getJSON("add_VAS2Unit.php?unit="+unit+"&timeline="+timeline_corrente+"&flag=eff&balanced="+value, function(data, status){            
        $.post("add_VAS2Unit_multiple.php", { unit:unit, timeline:timeline_corrente, flag:"eff", balanced:value}, function(data, status){       
              refreshUnitValues();          
        });
    });    
})




// questa funzione crea uno stato con il value dato e con la polarita data 
// aggiunge lo stato alla unit
//function setUnitValue (value, polarity, prepost, timeline){

    //create_state.php?at_stake=70
    //add_state.php?state=2707&timeline=9952&flag=pre
//}




// *******************************
// **** FUNZIONI DI CREAZIONE ****
// *******************************


function createUnit (unit, description, annotator) {
    if ($("#reference_unit").is(':checked')) {

        var reference = "reference";

    } else {var reference = "annotation";}
	
	if 
        //(((unit != "") && (description != "")) && ((unit != "type name") && (description != "type description")))
        ((unit != "") && (unit != "type name") && (annotator != ""))
    {	
        //setto la description alla stringa vuota se è rimasto il testo originale
        if (description == "type description") {description = "";}
		//document.getElementById('test').innerHTML = "hai selezionato: " + candidate_unit;		
		if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
		xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	
                /*
                var response = xmlhttp.responseText;
            	
            	//document.getElementById('test').innerHTML = response;
            	//document.getElementById('id_unit_corrente').value = response;
            	//document.getElementById('check').value = "ok";
            	//window.alert("creata la unit con id" + response);
     			document.getElementById('id_name_unit_corrente').innerHTML = '___';  
            	document.getElementById('description_unit_corrente').innerHTML = '___';  
                //id_create_unit
                //print_create_unit
                document.getElementById('id_create_unit').value = "type name";
                document.getElementById('print_create_unit').value = "type description";

                document.getElementById('id_echo_annotatore').innerHTML = annotator;
                document.getElementById('id_annotatore').value="";
                


                clear_pagina(); 
                clear_box_plan();  
                get_candidate_units();
                removeOptions(document.getElementById('id_select_unit'));

                $("#id_select_plan").empty();
                var init =  document.createElement("option");
                $(init).text("Select a plan");
                $(init).val("");
                $("#id_select_plan").append(init);
                get_units_select();
                */
                //window.alert("reference vale: " + reference);
                location.reload();


            	
			}
        }

        xmlhttp.open("GET","createUnit.php?print="+unit+"&"+"description="+description+"&"+"annotator="+annotator+"&ref="+reference,false);
        xmlhttp.send();	
	} else {
			window.alert ("devi inserire un nome per unit e annotatore"); 
			document.getElementById('check').value = "no";
			}    	            	
}            	
   

// ******* CREAZIONE DI UN AGENT ********

//id_create_agent
//id_print_create_agent
////create_agent.php?name=Ofelia&print=La+fiamma+di+Amleto
//id_button_new_agent
$(document).ready(function(){
    $("#id_button_new_agent").click(function(){

    //window.alert($("#id_select_plan").val());    
    
    if (($("#id_select_plan").val() == "") || ($("#id_select_plan").val() == "Select a plan")) 
    
    {window.alert("please select a plan \nto activate the management of plan elements")} else {
    
      if (($("#id_create_agent").val() != "type name") && ($("#id_create_agent").val() != "")){
		
		var plan = $("#id_select_plan").val();

        var agent_values = $("#id_values").val();

        //id_pleasant id_like id_dislike

        //var agent_pleasant = $("#id_pleasant").val();

        var agent_liking = $("#id_like").val();

        var agent_disliking = $("#id_dislike").val();

        if (agent_values ==  "type values separated by comma") {agent_values = "";}

        var agent_description = $("#id_print_create_agent").val();

        //window.alert("descrizione agente " + agent_description);

        if (agent_description == "type description") {agent_description = "";}
        
		//if (agent_pleasant == "type names separated by comma") {agent_pleasant = "";}

        if (agent_liking == "type names separated by comma") {agent_liking = "";}
        if (agent_disliking == "type names separated by comma") {agent_disliking = "";}

        if ($("#id_pleasant_create_yes").is(':checked'))
            {var agent_pleasant = 1;} else {var agent_pleasant = 0;}

        //window.alert ("pulsante= " +$("#id_pleasant_create_yes").is(':checked'));
		//window.alert ("create_agent.php?name="+$("#id_create_agent").val()+"&print="+agent_description+"&values="+agent_values+"&liking="+agent_liking+"&disliking="+agent_disliking+"&pleasant="+agent_pleasant);
    	
        $.get("create_agent.php?name="+$("#id_create_agent").val()+"&print="+agent_description+"&values="+agent_values+"&liking="+agent_liking+"&disliking="+agent_disliking+"&pleasant="+agent_pleasant, function(data, status){
        	
        	var id_nuovo_agent = parseInt(data);
        	var conta_menu = 0;
        	var index_corretto = 0;
        	//window.alert(id_nuovo_agent);
        	
        	$("#id_create_agent").val("type name");
			$("#id_print_create_agent").val("type description");
            $("#id_values").val("type values separated by comma");  
                    
        	
        	$("#id_select_agent").empty();
        	$.getJSON("get_agents.php", function(data, status){
        	var s0 = document.createElement("option");
        	$(s0).text("Select an agent");
        	$("#id_select_agent").append(s0);
        	
        	
        	
            $.each(data, function(i, field){
            	
            	conta_menu = conta_menu + 1;
            	//qui scorri l'array del piano per generare testo
            	var s = document.createElement("option");
            	$(s).text(field.name);
                //window.alert("creo option:" +  field.name + " " + field.id);
            	$(s).val(field.id);
            	
            	if (field.id == id_nuovo_agent){index_corretto = conta_menu; }
            	
            	
            	
            	$("#id_select_agent").append(s);  
            	//set the menu to the value (id) of the last added agent
            	// ...
            	            	
        		})
        		
        	//$("#id_select_agent").prop('selectedIndex',index_corretto);
        	$("#id_select_agent").prop('selectedIndex',0);
        		    
        	});

            

            // aggiorno i valori con quelli dell'agente
            // i valori selezionati devono restare tali
            // var current_atstake = $("#id_conflict_value").val();
            // var current_balanced = $("#id_support_value").val();

            refreshValues();
            /*
            $.getJSON("get_values.php?", function(data, status){
                
                $("#id_conflict_value").empty();
                $("#id_support_value").empty();
                
                                
                var s0 = document.createElement("option");
                $(s0).text("no value");
                $(s0).val("no");
                $("#id_conflict_value").append(s0);
                
                
                $.each(data, function(i, field){
                    //qui scorri l'array del piano per generare testo
                    var s = document.createElement("option");
                    var testo_select = field.name + " - " + field.nameAgent;
                    $(s).text(testo_select);
                    $(s).val(field.id);
                    $("#id_conflict_value").append(s);
                });
                
                if (current_atstake != "no") {
                $("#id_conflict_value").val(current_atstake);
                }
                
                var s0 = document.createElement("option");
                $(s0).text("no value");
                $(s0).val("no");
                $("#id_support_value").append(s0);

                                
                $.each(data, function(i, field){
                    //qui scorri l'array del piano per generare testo
                    var s = document.createElement("option");
                    var testo_select = field.name + " - " + field.nameAgent;
                    $(s).text(testo_select);
                    $(s).val(field.id);
                    $("#id_support_value").append(s);
                });
                
                if (current_balanced != "no") {
                $("#id_support_value").val(current_balanced);
                }
                
             });
            */
            


   		});
   		
      }	else {window.alert ("Please insert the agent's name and values");}      
     }  
    });
});



// ***** CREAZIONE DI UN GOAL *******

// CREATE GOAL
// id_button_new_goal
// id_create_goal
// id_print_create_goal
// create_goal.php?name=my+goal&print=descrizione+del+my+goal
// id_create_new_goal
$(document).ready(function(){
    $("#id_button_new_goal").click(function(){
    	
     if (($("#id_select_plan").val() == "") || ($("#id_select_plan").val() == "Select a plan")) 
            {window.alert("please select a plan \nto activate the management of plan elements")} 

     else {	      	
     
        if (($("#new_goal_name").val() != "type name") && ($("#new_goal_name").val() != "")){	

            // prima di creare un goal nuovo devo controllare che non esista già
            var current_plan = $("#id_select_plan").val();
        
            //si può creare il goal solo se il piano corrente ha un goal
            //quindi prima controllo
            $.getJSON("get_plan.php?plan="+current_plan, function(data, status){
                var plan_goal = data.goal;  
                //window.alert("plan goal: "+plan_goal);

                if (plan_goal == null){

    	           var print_goal = $("#new_goal_description").val();

                    if (print_goal == "type description") {print_goal ="";}
    	
    	           //window.alert("create_goal.php?name="+$("#id_create_goal").val()+"&print="+$("#id_print_create_goal").val());
                    $.get("create_goal.php?name="+$("#new_goal_name").val()+"&print="+print_goal, function(data, status){
        
                    var id_nuovo_goal =  parseInt(data);
                    var index_corretto = 0;
                    var conta_menu = 0;

                    $.get("add_goal2plan.php?goal="+id_nuovo_goal+"&plan="+$("#id_select_plan").val(), function(data, status){             
                        $.getJSON("get_goal_info.php?goal="+id_nuovo_goal, function(data, status){
                        $("#new_goal_description").val("");
                        $("#new_goal_name").val("");
                        $("#id_description_goal_corrente").html(data.print);
                        $("#id_name_goal").html(data.name);
                        //$("#id_select_goal").prop('selectedIndex',0);
                        //$("#id_select_goal").val(goal);
                        });                                
                    });            
                });
            
                } else {
                        window.alert("Please cancel the current goal before creating a new one");
                        $("#new_goal_description").val("");
                        $("#new_goal_name").val("");
                        }
            });
       
        } else {window.alert("Please insert a goal name");} 
    }  
    
    });
});







// addAction(document.incidents.add_incident.value,id_unit_corrente,document.incidents.add_incident.text)
// document.incidents.id_incident.value,document.incidents.print_incident.value, id_unit_corrente, id_timeline_corrente
function createAction (action, print, plan) {
	
  if (($("#id_select_plan").val() == "") || ($("#id_select_plan").val() == "Select a plan")) {window.alert("please select a plan \nto activate the management of plan elements")} else {
	
		//window.alert("create_action.php?action="+action+"&"+"print="+print);
	
		if (action != "") {
	
		//document.getElementById('test').innerHTML = "hai selezionato: " + candidate_unit;		
		if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
		xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	
            	//in response c'è l'id dell'azione creata
            	var response = xmlhttp.responseText;
            	//document.getElementById('test').innerHTML = response;
            	addAction(response,plan);
            	//get_actions(unit);
            	
            	//resetta campi
            	document.getElementById("id_print_incident").value = "";             
                document.getElementById('name_incident').value  = "";
            	
            	
			}
        }
        xmlhttp.open("GET","create_action.php?action="+action+"&"+"print="+print,false);
        xmlhttp.send();	    
	}   else {window.alert ("please insert an action name");}	  
  }          	
}      


// ***************************************
// FUNZIONI DI MODIFICA
// ***************************************


// ******** MODIFICA UNIT ***************

// tit_new id_button_new_unit_name --> id_name_unit_corrente
// description_new id_button_new_unit_description --> description_unit_corrente




// modifica nome unit
$(document).ready(function(){

    $("#id_button_new_unit_name").click(function(){
        var new_unit_name =   $("#tit_new").val();
        var current_unit = $("#id_select_unit").val();
        if ((current_unit != "select a unit to edit") && (new_unit_name != "")){       
            $.getJSON("change_unit_features.php?unit="+current_unit+"&new_name="+new_unit_name, function(data, status){
                
                //cambio il valore del campo interessato
                $("#id_name_unit_corrente").html(new_unit_name);
                //azzero il campo di testo della modifica
                $("#tit_new").val("");
                
                // AGGIORNO IL MENU DELLE UNIT
                $.getJSON("get_candidate_units.php", function(data, status){
                    
                    $("#id_select_unit").empty();
                    
                    var s0 = document.createElement("option");
                    $(s0).text("select a unit to edit");
                    //$(s0).val("no");
                    $("#id_select_unit").append(s0);
                
                
                    $.each(data, function(i, field){
                        //qui scorri l'array del piano per generare testo
                        var s = document.createElement("option");
                        var testo_select = field.print;
                        $(s).text(testo_select);
                        $(s).val(field.id);
                        $("#id_select_unit").append(s);
                    });
                    
                    $("#id_select_unit").val(current_unit);

                });
            });
        }
    });
});



function get_candidate_units_type (type){
  
     //var id_unit_corrente = document.getElementById('id_unit_corrente').value; 
     //var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; 
     //deleteUnit(id_unit_corrente, id_timeline_corrente);  
     clear_pagina(); 
     clear_pagina_low();  
     clear_box_plan();
      $("#id_value_atStake_before").empty();
      $("#id_value_inBalance_before").empty();
      $("#id_value_atStake_after").empty();
      $("#id_value_inBalance_after").empty();
     //location.reload();  

 
     /*
     if (type == "onlyunits") {$("#unitchoice_units").checked = true; } 
     if (type == "onlyrefs") {$("#unitchoice_refs").checked = true; } 
     */



     //resetPagina();
     //get_units_select (); 
     //resetPagina();
     $.getJSON("get_candidate_units.php?type="+type, function(data, status){
                
                var sel = document.getElementById("id_select_unit");
                var istr = document.createElement("option");

                $("#id_select_unit").empty();

                istr.text = "select a unit to edit";
                sel.add(istr);              
                
                //Cancello il precedente menu
                //removeOptions(document.getElementById("id_select_unit"));                 
                data.forEach(function(entry) {
                    var id = entry.id;
                    var printAction = entry.print;
                    var option = document.createElement("option");
                    var reference = entry.ref;
                    if (reference == "reference") {printAction = printAction + " [R]"}
                    option.text = printAction;
                    option.value = id;
                    sel.add(option);
                    }); 

            });    
}



// cambia tipo unit selezionate
$(document).ready(function(){
    $("#unitchoice_units").click(function(){

        get_candidate_units_type ("onlyunits");


    });
});

// cambia tipo unit selezionate
$(document).ready(function(){
    $("#unitchoice_refs").click(function(){

        get_candidate_units_type ("onlyrefs");


    });
});

// cambia tipo unit selezionate
$(document).ready(function(){
    $("#unitchoice_all").click(function(){

        get_candidate_units_type ("allunits");


    });
});



// modifica description unit
$(document).ready(function(){
    $("#id_button_new_unit_description").click(function(){
        var new_unit_description =   $("#description_new").val();
        var current_unit = $("#id_select_unit").val();
        if ((current_unit != "select a unit to edit") && (new_unit_description != "") ){    
            $.getJSON("change_unit_features.php?unit="+current_unit+"&new_description="+new_unit_description, function(data, status){
            //cambio il valore del campo interessato
                $("#description_unit_corrente").html(new_unit_description);
                $("#description_new").val("");
            });        
        }
    });
});

// ******** MODIFICA DI PLAN *****

/*
id_name_deplan_corrente
new_tit_plan
id_button_new_plan_name

description_deplan_corrente
new_description_plan
id_button_new_plan_description

name_action
new_action
id_button_new_action

*/

//modifica nome plan

$(document).ready(function(){

    $("#id_button_new_plan_name").click(function(){
        var new_plan_name =   $("#new_tit_plan").val();
        var current_plan = $("#id_select_plan").val();
        var current_unit = $("#id_select_unit").val();
        if ((current_plan != "") && (new_plan_name != "")){       
            $.getJSON("set_plan_features.php?plan="+current_plan+"&new_name="+new_plan_name, function(data, status){
                //cambio il valore del campo interessato
                $("#id_name_deplan_corrente").html(new_plan_name);
                //azzero campo modifica
                $("#new_tit_plan").val("");

                //aggiorno menu piani
                $.getJSON("get_unit_plans.php?unit="+current_unit, function(data, status){
    
                    $("#id_select_plan").empty();
                
                    var init =  document.createElement("option");
                    $(init).text("Select a plan");
                    $(init).val("");
                    $("#id_select_plan").append(init);
                    var my_index = 0;

                    $.each(data, function(i, field){
                        //qui scorri l'array dei piani estratto dal db per generare menu dei piani dopo la modifica
                        var s = document.createElement("option");
                        $(s).text(field.name);
                        $(s).val(field.id);
                        //window.alert (field.id);
                        $("#id_select_plan").append(s);
                        my_index++;
                        //if (field.id == nuovo_id) {nuovo_index = my_index;}                                 
                    });
                    $("#id_select_plan").val(current_plan);   
                }); // fine get_unit_plans   
            }); // fine set_plan_features
        }
    });
});


// modifica descrizione plan

$(document).ready(function(){

    $("#id_button_new_plan_description").click(function(){
        var new_plan_description =   $("#new_description_plan").val();
        var current_plan = $("#id_select_plan").val();
        if ((current_plan != "") && (new_plan_description != "")){       
            $.getJSON("set_plan_features.php?plan="+current_plan+"&new_description="+new_plan_description, function(data, status){
                //cambio il valore del campo interessato
                $("#description_deplan_corrente").html(new_plan_description);
                //azzero valore del campo modifica
                $("#new_description_plan").val("");
            });
        }
    });
});


$(document).ready(function(){
    $("#id_button_new_action").click(function(){
        var new_plan_action =  $("#new_action").val();
        //window.alert(new_plan_action);
        var new_plan_description =  $("#new_description_plan").val();
        var current_plan = $("#id_select_plan").val();
        //window.alert(current_plan);
        //get di id della action da cambiare

        // prendo l'agente del piano
        $.getJSON("get_plan.php?plan="+current_plan, function(data, status){       
            
            var current_action = data.action;
            //window.alert(current_action);

            if ((current_plan != "") && (new_plan_action != "")){ 
            	//window.alert("set_plan_features.php?action="+current_action+"&new_action="+new_plan_action);
                $.getJSON("set_plan_features.php?action="+current_action+"&new_action="+new_plan_action, function(data, status){
                    //cambio il valore del campo interessato
                    $("#name_action").html(new_plan_action);
                    //azzero valore del campo modifica
                    $("#new_action").val("");
                });
            }
        });    
    });
});

// *** modifica agente 

/*
id_name_agent_corrente
new_agent_name
id_button_new_agent_name

id_description_agent_corrente
new_agent_description
id_button_new_agent_description

id_print_values
new_agent_values
id_button_new_agent_values
*/

// permettere la modifica solo sul current plan agent.




$(document).ready(function(){
    $("#id_button_new_agent_name").click(function(){

        var plan = $("#id_select_plan").val();
        $.getJSON("get_plan.php?plan="+plan, function(data, status){       
            
            $("#id_description_plan_corrente").text(data.print);
            var current_agent = data.agent;
         
            //nuovo nome agente
            var new_agent_name =  $("#new_agent_name").val();
            //agente corrente         
            //var current_agent = $("#id_select_agent").val();
            //non fare nulla se non c'è un agente selezionato
            if ((current_agent != null) && (new_agent_name != "")){  

                $.getJSON("change_agent_features.php?agent="+current_agent+"&new_name="+new_agent_name, function(data, status){
                
                    //cambio il valore del campo interessato
                    $("#id_name_agent_corrente").html(new_agent_name);
                    //azzero il campo di modifica
                    $("#new_agent_name").val("");

                    // aggiornare lista valori at stake sopra e sotto!!
                        //window.alert ("ciccio pasticcio");
                        refreshValues();  
                        //non aggiorna sopra
                        refreshUnitValues();

                    // ora genero il nuovo menu agenti con il nome modificato
                    $.getJSON("get_agents.php", function(data, status){
                        $("#id_select_agent").empty();
                    
                        var s0 = document.createElement("option");
                        $(s0).text("Select an agent");
                        $("#id_select_agent").append(s0);
                    
                        $.each(data, function(i, field){
                            //qui scorri l'array del piano per generare testo
                            var s = document.createElement("option");
                            $(s).text(field.name);
                            $(s).val(field.id);
                            $("#id_select_agent").append(s);
                        })    
                        //$("#id_select_agent").val(current_agent);    
                    }); //fine creazione del menu degli agenti 
                });         
            }
        });    
    });
});


/*
id_description_agent_corrente
new_agent_description
id_button_new_agent_description
*/

$(document).ready(function(){

    $("#id_button_new_agent_description").click(function(){

        // recupero il piano per trovare l'agente associato, se c'è
        var plan = $("#id_select_plan").val();
        
        $.getJSON("get_plan.php?plan="+plan, function(data, status){       
            
            $("#id_description_plan_corrente").text(data.print);
            var current_agent = data.agent;


            var new_agent_description =   $("#new_agent_description").val();
        
            if ((current_agent != null) && (new_agent_description != "")){       
                
                $.getJSON("change_agent_features.php?agent="+current_agent+"&new_description="+new_agent_description, function(data, status){
                    //cambio il valore del campo interessato
                    $("#id_description_agent_corrente").html(new_agent_description);
                    //azzero campo di modifica
                    $("#new_agent_description").val("");
                });
            
            //$("#id_select_agent").val(current_agent);          
            }
        });
    });        
});


// **** modifica dei values *****

/*
id_print_values
new_agent_values
id_button_new_agent_values
*/


$(document).ready(function(){

    $("#id_button_new_agent_values").click(function(){

        // recupero il piano per trovare l'agente associato, se c'è
        var plan = $("#id_select_plan").val();
        
        // prendo l'agente del piano
        $.getJSON("get_plan.php?plan="+plan, function(data, status){       
            
            // a cosa serve questa riga?
            $("#id_description_plan_corrente").text(data.print);
            var current_agent = data.agent;


            var new_agent_values =   $("#new_agent_values").val();
        
            //verifico che l'agente del piano non sia vuoto che la stringa non sia vuota
            if ((current_agent != null) && (new_agent_values != "")){     

            //modifica dei valori con change_agent_values.php?agent=current_agent&new_values=new_agent_values
                $.getJSON("change_agent_values.php?agent="+current_agent+"&new_values="+new_agent_values, function(data, success){
                    
                    //cambio il valore del campo interessato
                    //con i nuovi valori
                    $.getJSON("get_agent_info.php?agent="+current_agent, function(data, status){
                        //window.alert(data.values);
                        $("#id_print_values").html(data.values);

                        // aggiornare lista valori at stake sopra e sotto!!
                        //window.alert ("ciccio pasticcio");
                        refreshValues();  
                        //non aggiorna sopra
                        refreshUnitValues();


                    }); 
                    //azzero campo di modifica
                    $("#new_agent_values").val("");


                });  
            
                // aggiorno il menu dei values
                refreshValues();    
            }
        });
    });        
});

// cambiamento pleasant
$(document).ready(function(){

    $("#id_button_new_agent_pleasant").click(function(){

        // recupero il piano per trovare l'agente associato, se c'è
        var plan = $("#id_select_plan").val();
        
        // prendo l'agente del piano
        $.getJSON("get_plan.php?plan="+plan, function(data, status){       
            
            //$("#id_description_plan_corrente").text(data.print);
            var current_agent = data.agent;

            var new_agent_pleasant =   $("#new_agent_pleasant").val();
        
            //verifico che l'agente del piano non sia vuoto che la stringa non sia vuota
            if ((current_agent != null) && (new_agent_values != "")){     

                //modifica dei valori con change_agent_values.php?agent=current_agent&new_values=new_agent_values
                $.getJSON("change_agent_features.php?agent="+current_agent+"&new_pleasant="+new_agent_pleasant, function(data, success){
                    
                        //cambio il valore del campo interessato
                        //con i nuovi valori
                        $.getJSON("get_agent_info.php?agent="+current_agent, function(data, status){
                        //window.alert(data.values);
                        $("#id_print_pleasant").html(data.pleasant);
                    }); 
                    
                    //azzero campo di modifica
                    $("#new_agent_pleasant").val("");

                });              
            }
        });
    });        
});

// cambiamento liking
$(document).ready(function(){

    $("#id_button_new_agent_like").click(function(){

        // recupero il piano per trovare l'agente associato, se c'è
        var plan = $("#id_select_plan").val();
        
        // prendo l'agente del piano
        $.getJSON("get_plan.php?plan="+plan, function(data, status){       
            
            //$("#id_description_plan_corrente").text(data.print);
            var current_agent = data.agent;

            var new_agent_liking =   $("#new_agent_like").val();
        
            //verifico che l'agente del piano non sia vuoto che la stringa non sia vuota
            if ((current_agent != null) && (new_agent_liking != "")){     

                //modifica dei valori con change_agent_values.php?agent=current_agent&new_values=new_agent_values
                $.getJSON("change_agent_features.php?agent="+current_agent+"&new_liking="+new_agent_liking, function(data, success){
                    
                        //cambio il valore del campo interessato
                        //con i nuovi valori
                        $.getJSON("get_agent_info.php?agent="+current_agent, function(data, status){
                        //window.alert(data.values);
                        $("#id_print_like").html(data.liking);
                    }); 
                    
                    //azzero campo di modifica
                    $("#new_agent_like").val("");

                });              
            }
        });
    });        
});

// cambiamento disliking
$(document).ready(function(){

    $("#id_button_new_agent_dislike").click(function(){

        // recupero il piano per trovare l'agente associato, se c'è
        var plan = $("#id_select_plan").val();
        
        // prendo l'agente del piano
        $.getJSON("get_plan.php?plan="+plan, function(data, status){       
            
            //$("#id_description_plan_corrente").text(data.print);
            var current_agent = data.agent;

            var new_agent_disliking =   $("#new_agent_dislike").val();
        
            //verifico che l'agente del piano non sia vuoto che la stringa non sia vuota
            if ((current_agent != null) && (new_agent_disliking != "")){     

                //modifica dei valori con change_agent_values.php?agent=current_agent&new_values=new_agent_values
                $.getJSON("change_agent_features.php?agent="+current_agent+"&new_disliking="+new_agent_disliking, function(data, success){
                    
                        //cambio il valore del campo interessato
                        //con i nuovi valori
                        $.getJSON("get_agent_info.php?agent="+current_agent, function(data, status){
                        //window.alert(data.values);
                        $("#id_print_dislike").html(data.disliking);
                    }); 
                    
                    //azzero campo di modifica
                    $("#new_agent_dislike").val("");

                });              
            }
        });
    });        
});





function refreshValues (){

            // prima memorizzo i valori correnti
            var current_atstake_p = $("#id_conflict_value_p").val();
            var current_balanced_p = $("#id_support_value_p").val();
            var current_atstake_e = $("#id_conflict_value_e").val();
            var current_balanced_e = $("#id_support_value_e").val();

            // recupero tutti i valori di tutti gli agenti
            // e genero i menu
            $.getJSON("get_values.php", function(data, status){
                
                // vuoto i quattro menu
                $("#id_conflict_value_p").empty();
                $("#id_support_value_p").empty();
                $("#id_conflict_value_e").empty();
                $("#id_support_value_e").empty();
                
                                
                /*
                var s0 = document.createElement("option");
                $(s0).text("no value");
                $(s0).val("no");
                $("#id_conflict_value_p").append(s0);
                */
                
                
                $.each(data, function(i, field){
                    //qui scorri l'array del piano per generare testo
                    var s = document.createElement("option");
                    var testo_select = field.name + " - " + field.nameAgent;
                    $(s).text(testo_select);
                    $(s).val(field.id);
                    $("#id_conflict_value_p").append(s);
                    if (field.id == current_atstake_p) {$("#id_conflict_value_p").val(current_atstake_p);}
                });
                
                //if (current_atstake != "no") {
                //$("#id_conflict_value").val(current_atstake);
                //}
                
                /*
                var s0 = document.createElement("option");
                $(s0).text("no value");
                $(s0).val("no");
                $("#id_support_value_p").append(s0);
                */

                                
                $.each(data, function(i, field){
                    //qui scorri l'array del piano per generare testo
                    var s = document.createElement("option");
                    var testo_select = field.name + " - " + field.nameAgent;
                    $(s).text(testo_select);
                    $(s).val(field.id);
                    $("#id_support_value_p").append(s);
                    if (field.id == current_balanced_p) {$("#id_support_value_p").val(current_balanced_p);}
                });
                
                //if (current_balanced != "no") {
                //$("#id_support_value").val(current_balanced);
                //}
                

                /*
                var s0 = document.createElement("option");
                $(s0).text("no value");
                $(s0).val("no");
                $("#id_conflict_value_e").append(s0);
                */
                
                
                $.each(data, function(i, field){
                    //qui scorri l'array del piano per generare testo
                    var s = document.createElement("option");
                    var testo_select = field.name + " - " + field.nameAgent;
                    $(s).text(testo_select);
                    $(s).val(field.id);
                    $("#id_conflict_value_e").append(s);
                    if (field.id == current_atstake_e) {$("#id_conflict_value_e").val(current_atstake_e);}
                });
                
                //if (current_atstake != "no") {
                //$("#id_conflict_value").val(current_atstake);
                //}
                
                /*
                var s0 = document.createElement("option");
                $(s0).text("no value");
                $(s0).val("no");
                $("#id_support_value_e").append(s0);
                */

                                
                $.each(data, function(i, field){
                    //qui scorri l'array del piano per generare testo
                    var s = document.createElement("option");
                    var testo_select = field.name + " - " + field.nameAgent;
                    $(s).text(testo_select);
                    $(s).val(field.id);
                    $("#id_support_value_e").append(s);
                    if (field.id == current_balanced_e) {$("#id_support_value_e").val(current_balanced_e);}
                });


             });

}

// modifica goal

/*
id_name_goal
new_goal_name
id_button_new_goal_name
*/

$(document).ready(function(){

    $("#id_button_new_goal_name").click(function(){

        // recupero il piano per trovare l'agente associato, se c'è
        var plan = $("#id_select_plan").val();
        
        // prendo l'agente del piano
        $.getJSON("get_plan.php?plan="+plan, function(data, status){       
            
            $("#id_description_plan_corrente").text(data.print);
            var current_goal = data.goal;


            var new_goal_name =   $("#new_goal_name").val();
        
            //verifico che l'agente del piano non sia vuoto che la stringa non sia vuota
            if ((current_goal != null) && (new_goal_name != "")){     

            //modifica del goal con change_goal_features.php?
                $.getJSON("change_goal_features.php?goal="+current_goal+"&new_name="+new_goal_name, function(data, success){
                    
                    //cambio il valore del campo interessato
                    //con il nuovo nome
                    $("#id_name_goal").html(new_goal_name);
                    //refreshGoals();
                    //azzero il campo di modifica
                    $("#new_goal_name").val("");

                });  
                          
            }
        });
    });        
});


function refreshGoals () {
// ricarico il menu dei goal
    $.getJSON("get_goals.php", function(data, status){
        //window.alert ("passo di qui") ;
        $("#id_select_goal").empty();
            
        var s0 = document.createElement("option");
        $(s0).text("Select a goal");
        $("#id_select_goal").append(s0);

        $.each(data, function(i, field){
            //qui scorri l'array dei goal per generare descrizione
            //window.alert ("creo option per " + field.name + " " + field.id);
            var s = document.createElement("option");
            $(s).text(field.name);
            $(s).val(field.id);
            $("#id_select_goal").append(s);
        }) 
    });
}

/*
id_description_goal_corrente
new_goal_description
id_button_new_goal_description
*/

$(document).ready(function(){

    $("#id_button_new_goal_description").click(function(){

        // recupero il piano per trovare l'agente associato, se c'è
        var plan = $("#id_select_plan").val();
        
        // prendo l'agente del piano
        $.getJSON("get_plan.php?plan="+plan, function(data, status){       
            
            $("#id_description_plan_corrente").text(data.print);
            var current_goal = data.goal;


            var new_goal_description =   $("#new_goal_description").val();
        
            //verifico che l'agente del piano non sia vuoto che la stringa non sia vuota
            if ((current_goal != null) && (new_goal_description != "")){     

            //modifica dei valori con change_agent_values.php?agent=current_agent&new_values=new_agent_values
                $.getJSON("change_goal_features.php?goal="+current_goal+"&new_description="+new_goal_description, function(data, success){
                    
                    //cambio il valore del campo interessato
                    //con il nuovo nome
                    $("#id_description_goal_corrente").html(new_goal_description);
                    //azzero campo di modifica
                    $("#new_goal_description").val("");

                });  
                          
            }
        });
    });        
});




// ***********************************
// **** FUNZIONI DI CANCELLAZIONE ****
// ***********************************




// ****** CANCELLAZIONE PIANO **********


// id_button_delete_plan

// cancellazione piano
// si cancella il piano in focus
// $id_plan = $_REQUEST["plan"];
// id_delete_plan
// //delete_plan_from_repository.php?plan=7
$(document).ready(function(){
    $("#id_button_delete_plan").click(function(){
    	// cancello il piano
    	var plan_to_del = $("#id_select_plan").val();
    	//alert("delete_plan_from_repository.php?plan="+plan_to_del);
        $.getJSON("delete_plan_from_repository.php?plan="+plan_to_del, function(data, success){
                
        	
 		var unit = $("#id_select_unit").val();
        $.getJSON("get_unit_plans.php?unit="+unit, function(data, status){	

                //window.alert(data);				
    			
    			$("#description_deplan_corrente").html("____");
            	$("#id_name_deplan_corrente").html("____");
            	$("#name_action").html("____");
    			
    			$("#id_select_plan").empty();
    			var init =  document.createElement("option");
        		$(init).text("Select a plan");
            	$(init).val("");
            	$("#id_select_plan").append(init);
        		
            		$.each(data, function(i, field){
            		//qui scorri l'array del piano per generare testo

            		var s = document.createElement("option");
            		$(s).text(field.name);
            		$(s).val(field.id);
            		$("#id_select_plan").append(s);
        			})
        					    
        		});       		
        	clear_pagina_low();	     
        }); 
  	});
});





//id_button_delete_action


$(document).ready(function(){
    $("#id_button_delete_action").click(function(){
  
  	 deleteActionFromRep(document.getElementById('cand_incident').value);
 	 get_actions(document.getElementById('id_select_plan').value);
     document.getElementById("id_print_incident").value = "";             
     document.getElementById('name_incident').value  = "";
    	
    	
    });
})


// *********** CANCELLAZIONE DI UNA AZIONE ************

function deleteActionFromRep (action) {
	    //window.alert("delete_action_from_repository.php?action="+action);
		//document.getElementById('test').innerHTML = "hai selezionato: " + candidate_unit;		
		
	if (action == "select an action") {window.alert("please select an action from the repository");} else { 
		
		
		if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
		xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	var response = xmlhttp.responseText;
            	//document.getElementById('test').innerHTML = response;
            	//get_actions(unit);
            	
            	
            	//window.alert("passo di qui");
                //document.getElementById("cur_incident").innerHTML = ""; 
                document.getElementById('id_name_action_corrente').innerHTML = '____';
        		document.getElementById('id_description_action_corrente').innerHTML = '____';


            	
            	
			}
        }
        xmlhttp.open("GET","delete_action_from_repository_unit.php?action="+action,false);
        xmlhttp.send();		            	
	}
}       
 




function deleteAction (unit,action){
	//window.alert("delete_action.php?unit="+unit+"&"+"action="+action);
	if (document.getElementById('check').value != "no"){
		//document.getElementById('test').innerHTML = "hai selezionato: " + candidate_unit;		
		if (action == "select an action"){window.alert("please select an action to be removed from the unit");} else {
		
		if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
		xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	var response = xmlhttp.responseText;
            	//document.getElementById('test').innerHTML = response; 
            	
            	
            	//removeOptions(document.getElementById("curr_incident"));
            	removeOptions(document.getElementById("cand_incident"));
            	
            	//window.alert("passo di qui");
                //document.getElementById("cur_incident").innerHTML = ""; 
                document.getElementById("id_print_incident").value = "";             
                document.getElementById('name_incident').value  = "";

            	
            	get_actions(unit);
                                    
            }
        }
        xmlhttp.open("GET","delete_action.php?unit="+unit+"&"+"action="+action,false);
        xmlhttp.send();	
	}
	}
}


function deleteUnit (unit, timeline) {
		//document.getElementById('test').innerHTML = "hai selezionato: " + candidate_unit;
        //window.alert("cancello la unit " + unit + "nella timeline " + timeline);
		if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
		xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	var response = xmlhttp.responseText;         	
            	//document.getElementById('test').innerHTML = response;
            	//document.getElementById('id_unit_corrente').value = response;	
 
 				            	
            	//id_name_unit_corrente
				//description_unit_corrente
            	
            	
            	//document.getElementById('id_name_unit_corrente').innerHTML = '___';  
            	//document.getElementById('description_unit_corrente').innerHTML = '___';  

            	
            	//document.getElementById('check').value = "no";
            	//removeOptions(document.getElementById('id_select_unit'));
            	
            	location.reload();
			}
        }
        xmlhttp.open("GET","delete_unit.php?unit="+unit+"&"+"timeline="+timeline,false);
        xmlhttp.send();		            	
}     

/*
$.get("map_plan_unit.php?plan="+plan+"&unit="+new_mapping_unit, function(data, status){
        		
        		$("#candidate_mapping_last").prop('selectedIndex',0);
            	
        		});

*/


// ********* CANCELLAZIONE DI UN AGENT ***********

//id_button_delete_agent
//delete_agent_from_repository.php?agent=7
//verificare 
//detach_agent_from_plan.php?agent=7&plan=3 
$(document).ready(function(){
    $("#id_button_delete_agent").click(function(){
    	// cancello l'agente
    	var agent_to_del = $("#id_select_agent").val();
        var current_plan = $("#id_select_plan").val();

        //cancello agent dalla visualizzazione solo se è l'agent
        //del piano corrente

         $.getJSON("get_plan.php?plan="+current_plan, function(data, status){
            var plan_agent = data.agent;      
            if (plan_agent == agent_to_del) {     
                // cancello nome e descrizione dell'agent solo se è quello del piano corrente 
                $("#id_description_agent_corrente").html("___");  
                $("#id_name_agent_corrente").html("___");    
                $("#id_print_values").html('___');   
                }
        });

    	//window.alert("cancello agent " + agent_to_del);
    	if (agent_to_del == "Select an agent"){window.alert("devi selezionare un agent da cancellare")} else {
    	
        $.get("delete_agent_from_repository.php?agent="+$("#id_select_agent").val(), function(data, status){

        	/*
        	// cancello il goal da tutti i piani
        	$.getJSON("get_plan.php?plan="+$("#id_select_plan").val(), function(data, status){
        	
        	//window.alert (data.agent);
            //$("#id_description_plan_corrente").text(data.print);
            var agent = data.agent;   
            if (agent == agent_to_del) {
            	// 	detach_agent_from_plan.php?agent=7&plan=3 
            		$.get("detach_agent_from_plan.php?name="+$("#id_select_agent").val(), function(data, status){
        			//$("#plans").empty();
        			});
            	}
            // ricarico il menu degli agenti
        	});
			*/
		
			$.getJSON("get_agents.php", function(data, status){
        			$("#id_select_agent").empty();
        			
        			var s0 = document.createElement("option");
        			$(s0).text("Select an agent");
        			$("#id_select_agent").append(s0);
        			
            			$.each(data, function(i, field){
            			//qui scorri l'array del piano per generare testo
            			var s = document.createElement("option");
            			$(s).text(field.name);
            			$(s).val(field.id);
            			$("#id_select_agent").append(s);
            			//$("#id_description_agent_corrente").html("___");
        				})    
        		});  

            refreshValues();

            /*
            $("#id_description_agent_corrente").html("___");  
        	$("#id_name_agent_corrente").html("___");    
            $("#id_print_values").html('___'); 
            */

            // aggiorno i valori dato che possono esserne stati cancellati
            // i valori selezionati devono restare tali
            

            /*
            var current_atstake_p = $("#id_conflict_value_p").val();
            var current_balanced_p = $("#id_support_value_p").val();
            var current_atstake_e = $("#id_conflict_value_e").val();
            var current_balanced_e = $("#id_support_value_e").val();


            $.getJSON("get_values.php?", function(data, status){
                
                $("#id_conflict_value_p").empty();
                $("#id_support_value_p").empty();
                $("#id_conflict_value_e").empty();
                $("#id_support_value_e").empty();
                
                                
                var s0 = document.createElement("option");
                $(s0).text("no value");
                $(s0).val("no");
                $("#id_conflict_value_p").append(s0);
                if (current_atstake_p == "no"){$("#id_conflict_value_p").val("no");}
                
                
                $.each(data, function(i, field){
                    //qui scorri l'array del piano per generare testo
                    var s = document.createElement("option");
                    var testo_select = field.name + " - " + field.nameAgent;
                    $(s).text(testo_select);
                    $(s).val(field.id);
                    //window.alert("id vale: " + field.id + " e " + "current_atstake vale: " + current_atstake);
                    $("#id_conflict_value_p").append(s);
                    if (field.id == current_atstake_p) {
                        $("#id_conflict_value_p").val(current_atstake_p);
                    }
                });
                
                //setto nuovamente il menu alla voce giusta
        
                var s0 = document.createElement("option");
                $(s0).text("no value");
                $(s0).val("no");
                $("#id_support_value_p").append(s0);
                if (current_balanced_p == "no"){$("#id_support_value_p").val("no");}

                                
                $.each(data, function(i, field){
                    //qui scorri l'array del piano per generare testo
                    var s = document.createElement("option");
                    var testo_select = field.name + " - " + field.nameAgent;
                    $(s).text(testo_select);
                    $(s).val(field.id);
                    $("#id_support_value_p").append(s);
                    if (field.id == current_balanced_p) {
                        $("#id_support_value_p").val(current_balanced_p);
                    }
                });
                
                //if (current_balanced != "no") {
                //$("#id_support_value").val(current_balanced);
                //}
                
             });

            */

    });
    }
  });   
});


// ********* CANCELLAZIONE DEI GOAL **********

// DELETE GOAL
// id_goal_delete
// delete_agent_from_repository.php?agent=7
// verificare 
// detach_goal_from_plan.php?agent=7&plan=3 
$(document).ready(function(){
    $("#id_goal_delete").click(function(){
    	// cancello il goal
    	//var goal_to_del = $("#id_select_goal").val();
        var current_plan = $("#id_select_plan").val();
    	
         //si può eliminare il goal solo se il piano corrente ha un goal
         //quindi prima controllo
        $.getJSON("get_plan.php?plan="+current_plan, function(data, status){
            var plan_goal = data.goal;   
            //window.alert ("goal del piano: " + plan_goal);     
            if (plan_goal != "") {     
                // cancello nome e descrizione del goal corrente      
                
                $.get("delete_goal_from_repository.php?goal="+plan_goal, function(data, status){
                    //window.alert("delete_goal_from_repository.php?goal="+plan_goal);
                    $("#id_name_goal").html("___");  
                    $("#id_description_goal_corrente").html("___");   
                }); 
            }
	   });          
    }); 
});









 
// **** UTILITIES ****

function removeOptions(selectbox)
{
    var i;
    for(i=selectbox.options.length-1;i>=0;i--)
    {
        selectbox.remove(i);
    }
}



// id_button_new_annotator

$(document).ready(function(){

    $("#id_button_new_annotator").click(function(){
        var current_unit = $("#id_select_unit").val();
        var new_annotator = $("#id_annotatore").val();
        if ((current_unit != "select a unit to edit") && (new_annotator != "")){       
            $.getJSON("change_annotator.php?unit="+current_unit+"&annotator="+new_annotator, function(data, status){
                
               //cambio nome annotatore 
               document.getElementById('id_echo_annotatore').innerHTML = new_annotator;
               $("#id_annotatore").val('');
            
            });
        } else  {
                    window.alert("devi selezionare una unit e inserire un nuovo nome per l'annotatore");
                }
    });
});
 


// MENU SELECT2
// MENU CON SELEZIONE MULTIPLA


$(document).ready(function() {
  //$(".js-example-basic-multiple").select2();
  //placeholder: "Select a value"

$(".js-example-basic-multiple").select2({
  placeholder: "Select a value"
});

});

// **** MODIFICHE 23/2/2018 *****

// #type_unit_corrente è un campo nascosto dove si memorizza il tipo delle unit, se annotation o reference
/*
var unit_type = $("#type_unit_corrente").val();
            var plan_type = "Base"; 
            //"Rec" | "Base"

            if (unit_type == "annotation") {plan_type="Rec";}
*/

// #id_select_plan contiene il piano correntemente selezionato



function get_candidate_plans (unit,type) {
        //window.alert ("cerco i piani candidati per la unit " + unit + " che ha tipo " + type) ;
    
        //document.getElementById('test').innerHTML = "hai selezionato: " + candidate_unit;     
        if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                //var response = xmlhttp.responseText;
                //document.getElementById('test').innerHTML = response;             
                
                var my_response = JSON.parse(xmlhttp.responseText);
                //var a = my_response.children;
                var ca = my_response;
                //var dopo = a;
           
                
                
                //removeOptions(document.getElementById("id_curr_subplans"));
                removeOptions(document.getElementById("id_cand_subplan"));
                //removeOptions(document.getElementById("id_dopo"));
                
                //var s1 = document.getElementById("id_curr_subplans");
                var s2 = document.getElementById("id_cand_subplan");
                //var s3 = document.getElementById("id_dopo");
                
                //var istr1 = document.createElement("option");
                var istr2 = document.createElement("option");
                //var istr3 = document.createElement("option");
                //var istr4 = document.createElement("option");
                //istr1.text = "select a subplan";
                istr2.text = "select a candidate subplan";
                //istr3.text = "inserisci dopo ...";
                //istr3.value = "missing";
                //istr4.text = "inserisci per primo";
                //istr4.value = "primo";
                //s1.add(istr1);
                s2.add(istr2);
                //s3.add(istr3);
                //s3.add(istr4);
                
                
                
                //{"id":"38","print":"no need to argue","name":"piano B","type":"Base","timeline":null,"agent":null,"goal":null,"action":null}
                
                /*
                
                $('#id_display_subplans').text("");
                a.forEach(function(entry) {
                    
                    //window.alert ("current subplan");
                    var id = entry.id;
                    var printPlan = entry.name;
                    var option = document.createElement("option");
                    option.text = printPlan;
                    option.value = id;
                    s1.add(option);
                    
                    //Scrivo i piani nel box apposito
                    //id_display_subplans
                    
                    
                    $('#id_display_subplans').append(printPlan + " ");
                    
                    
                    }); 
                */    
                
                ca.forEach(function(entry) {
                    
                    //window.alert ("candidate subplan");
                    var id = entry.id;
                    var printPlan = entry.name;
                    var option = document.createElement("option");
                    option.text = printPlan;
                    option.value = id;
                    s2.add(option);
                    });
                    
                
                /*
                dopo.forEach(function(entry) {
                    
                    //window.alert ("current subplan");
                    var id = entry.id;
                    var printPlan = entry.name;
                    var option = document.createElement("option");
                    option.text = printPlan;
                    option.value = id;
                    s3.add(option);
                    }); 
                */    
                
    
                
                
            }
        }
        xmlhttp.open("GET","get_candidate_plans_ups.php?unit="+unit+"&type="+type,false);
        xmlhttp.send();     
}








 
