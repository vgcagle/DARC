


/*


TO DO

CONTROLLA CANCELLAZIONE DEL PIANO




*/

// js jquery functions 

// al caricamento saranno chiamate tutte le funzioni della parte inferiore e superiore
// serve una funzione di caricamento generale


// ************ AGENTS *********************************************************************


// creazione di menu degli agenti 
// id_select_agent
// get_agents.php
// [{"id":"1","print":"amico del giaguaro","name":"Amleto"},{"id":"2","print":"la sciacquetta","name":"Ofelia"},{"id":"3","print":"il cattivo","name":"Claudio"}]

$(document).ready(function(){
   
   	$("#id_select_absplan").change(function(){ 	
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

// stampa i dati dell'agente selezionato nei campi (textarea)
// id_agent_id e id_agent_print
// get_agent_info.php?agent=3
// {"print":"il cattivo","name":"Claudio","id":"3"}

/*
$(document).ready(function(){
    $("#id_select_agent").change(function(){
        $.getJSON("get_agent_info.php?agent="+$("#id_select_agent").val(), function(data, status){
        	//$("#plans").empty();
            //$("#id_agent_id").html(data.name);
            $("#id_description_agent_corrente").html(data.print);
            $("#id_name_agent_corrente").html(data.name);
        });
    });
});
*/


$(document).ready(function(){
    $("#id_button_add_agent").click(function(){
    	
      	if ($("#id_select_absplan").val() != "" && $("#id_select_agent").val() != "Select an agent" ) {
    		//window.alert("add_agent2plan.php?agent="+$("#id_select_agent").val() + "&plan="+$("#id_select_absplan").val());
            $.get("add_agent2plan.php?agent="+$("#id_select_agent").val() + "&plan="+$("#id_select_absplan").val(), function(data, status){                             	   		
            	$.getJSON("get_agent_info.php?agent="+$("#id_select_agent").val(), function(data, status){
	
					 $("#id_description_agent_corrente").html(data.print);
            		 $("#id_name_agent_corrente").html(data.name);
            		 $("#id_select_agent").prop('selectedIndex',0);
	
            	});            	
        });
        
    	} else {window.alert ("Please select an agent and a plan");};
    });
});




//id_create_agent
//id_print_create_agent
////create_agent.php?name=Ofelia&print=La+fiamma+di+Amleto
//id_button_new_agent
$(document).ready(function(){
    $("#id_button_new_agent").click(function(){
    	
      if ($("#id_create_agent").val() != "insert new agent name" && $("#id_create_agent").val() != ""){

    	
        $.get("create_agent.php?name="+$("#id_create_agent").val()+"&print="+$("#id_print_create_agent").val(), function(data, status){
        	
        	var id_nuovo_agent = parseInt(data);
        	var conta_menu = 0;
        	var index_corretto = 0;
        	//window.alert(id_nuovo_agent);
        	
        	$("#id_create_agent").val("type name");
			$("#id_print_create_agent").val("type description");
                    
        	
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
        		
        	$("#id_select_agent").prop('selectedIndex',index_corretto);
        		    
        	});
   		});
   		
      }	else {window.alert ("devi inserire un nome per l'agente");}      
    });
});



//id_button_delete_agent
//delete_agent_from_repository.php?agent=7
//verificare 
//detach_agent_from_plan.php?agent=7&plan=3 
$(document).ready(function(){
    $("#id_button_delete_agent").click(function(){
    	// cancello l'agente
    	var agent_to_del = $("#id_select_agent").val();
    	//window.alert("cancello agent " + agent_to_del);
        $.get("delete_agent_from_repository.php?agent="+$("#id_select_agent").val(), function(data, status){

        	/*
        	// cancello il goal da tutti i piani
        	$.getJSON("get_plan.php?plan="+$("#id_select_absplan").val(), function(data, status){
        	
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
        	$("#id_description_agent_corrente").html("___");                
    });
  });   
});

// ************ UNITS *********************************************************************

// creazione del menu delle unit
// id_mapping_unit
// get_units_candidate.php
// [{"id":"1","print":"Unit1"},{"id":"2","print":"Unit2"},{"id":"3","print":"Unit3"}


//id="candidate_mapping_first"
//id="candidate_mapping_last"

$(document).ready(function(){
   // $("body").load(function(){})
   	$("#id_select_absplan").change(function(){ 	
        $.getJSON("get_units_candidate.php", function(data, status){
        	
        	$("#candidate_mapping_first").empty();
        	var init =  document.createElement("option");
        	$(init).text("select a unit");
            $(init).val("");
            $("#candidate_mapping_first").append(init);
        	
            $.each(data, function(i, field){
            	//qui scorri l'array del piano per generare testo
            	var s = document.createElement("option");
            	$(s).text(field.print);
            	$(s).val(field.id);
            	$("#candidate_mapping_first").append(s);
        	})  
        	
        	$("#candidate_mapping_last").empty();
        	var init =  document.createElement("option");
        	$(init).text("select a unit");
            $(init).val("");
            $("#candidate_mapping_last").append(init);
        	
            $.each(data, function(i, field){
            	//qui scorri l'array del piano per generare testo
            	var s = document.createElement("option");
            	$(s).text(field.print);
            	$(s).val(field.id);
            	$("#candidate_mapping_last").append(s);
        	})    
        });
    });
});

// stampa la print della unit selezionata nel campo (textarea)
// id_mapping_unit_print
// get_unit.php?unit=2
// {"timeline":"2","description":"la unit 2","name":"Unit2"}

//campo_mapping_first
//description_mapping_first
// id="candidate_mapping_first"
// id="candidate_mapping_last"

$(document).ready(function(){
    $("#id_button_mapping_first").click(function(){
    	
    	
       if ($("#id_select_absplan").val() != ""){
    	
    	var new_mapping_unit = $("#candidate_mapping_first").val();
    	
    	
        $.getJSON("get_unit.php?unit="+$("#candidate_mapping_first").val(), function(data, status){
        	//$("#plans").empty();
        	//id_campo_mapping_unit
        	$("#campo_mapping_first").html(data.name);
            $("#description_mapping_first").text(data.description);
            var plan = $("#id_select_absplan").val();
            
        	////map_plan_unit.php?plan=3$unit=5
        	//window.alert("map_plan_unit.php?plan="+plan+"&unit="+$("#id_candidate_mapping").val());
        	//window.alert("map_absplan_unit.php?plan="+plan+"&unit="+new_mapping_unit + "&flag=init");
        	
        	//window.alert("map_absplan_unit.php?plan="+plan+"&unit="+new_mapping_unit + "&flag=init");
        	$.get("map_absplan_unit.php?plan="+plan+"&unit="+new_mapping_unit + "&flag=init", function(data, status){
        		
        		$("#candidate_mapping_first").prop('selectedIndex',0);
        		//window.alert("ho aggiunto la unit!");
            	//verifica status e poi dai ok
        		});
        });        
    	
       } else {window.alert("devi selezionare un piano");}
    
    }); 
    
});


//id="candidate_mapping_first"
//id="candidate_mapping_last"
$(document).ready(function(){
	
    $("#id_button_mapping_last").click(function(){
    	
      if ($("#id_select_absplan").val() != ""){	
    	
    	var new_mapping_unit = $("#candidate_mapping_last").val();
        $.getJSON("get_unit.php?unit="+$("#candidate_mapping_last").val(), function(data, status){
        	//$("#plans").empty();
        	//id_campo_mapping_unit
        	$("#campo_mapping_last").html(data.name);
            $("#description_mapping_last").html(data.description);
            var plan = $("#id_select_absplan").val();
        	////map_plan_unit.php?plan=3$unit=5
        	//window.alert("map_plan_unit.php?plan="+plan+"&unit="+$("#id_candidate_mapping").val());
        	//window.alert("map_plan_unit.php?plan="+plan+"&unit="+new_mapping_unit);
        	$.get("map_absplan_unit.php?plan="+plan+"&unit="+new_mapping_unit + "&flag=end", function(data, status){
        		
        		$("#candidate_mapping_last").prop('selectedIndex',0);
            	//verifica status e poi dai ok
        		});
        });    
        
      } else {window.alert("devi selezionare un piano");}  
            
    });
});




// ************ GOALS *********************************************************************

// creazione del menu dei goal
// id_select_goal
// get_goals.php
// [{"id":"1","print":"Vedere la partita","name":""},{"id":"2","print":"Farsi un panino","name":"panino"}

$(document).ready(function(){
   // $("body").load(function(){})
   $("#id_select_absplan").change(function(){ 	
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

// stampa la print del goal selezionato nel campo (textarea)
// id_description_goal_corrente
// get_goal.php?goal=2
// {"print":"Farsi un panino","name":"panino","id":"2"}

/*
$(document).ready(function(){
    $("#id_select_goal").change(function(){
        $.getJSON("get_goal_info.php?goal="+$("#id_select_goal").val(), function(data, status){
            $("#id_description_goal_corrente").html(data.print);
        });
        //
        //var plan = $("#id_select_absplan").val();
        ////window.alert ("add_goal2plan.php?goal="+$("#id_select_goal").val()+"&plan="+plan);
        //$.getJSON("add_goal2plan.php?goal="+$("#id_select_goal").val()+"&plan="+plan, function(data, status){
        //	// add_goal2plan.php?goal=3&plan=5
        //    //verifica status e poi dai ok
        //});
        //
    });
});
*/


// id="id_goal_add"
// id="id_goal_delete"






// CREATE GOAL
// id_button_new_goal
// id_create_goal
// id_print_create_goal
// create_goal.php?name=my+goal&print=descrizione+del+my+goal
// id_create_new_goal
$(document).ready(function(){
    $("#id_button_new_goal").click(function(){
    	
     if ($("#id_create_goal").val() != "insert new goal name" && $("#id_create_goal").val() != ""){	
    	
    	
    	//window.alert("create_goal.php?name="+$("#id_create_goal").val()+"&print="+$("#id_print_create_goal").val());
        $.get("create_goal.php?name="+$("#id_create_goal").val()+"&print="+$("#id_print_create_goal").val(), function(data, status){
            //$("#id_agent_id").html(data.name);
        
            var id_nuovo_goal =  parseInt(data);
            var index_corretto = 0;
            var conta_menu = 0;
            
        	$("#id_create_goal").val("type name");
			$("#id_print_create_goal").val("type description");
            
            
            $.getJSON("get_goals.php", function(data, status){
        	$("#id_select_goal").empty();
        	
        	var s0 = document.createElement("option");
        	$(s0).text("Select a goal");
        	$("#id_select_goal").append(s0);
        	
            $.each(data, function(i, field){
            	
            	conta_menu = conta_menu + 1;
            	//qui scorri l'array dei goal per generare descrizione
            	var s = document.createElement("option");
            	$(s).text(field.name);
            	$(s).val(field.id);
            	$("#id_select_goal").append(s);
            	
            	
            	if (field.id == id_nuovo_goal){index_corretto = conta_menu; }
            	
            	
            	
            	    	
            	
        		})    
        		
        		$("#id_select_goal").prop('selectedIndex',index_corretto);

        		
        	
        	});
            
        });
       
     } else {window.alert("Insert a goal name");} 
    });
});


// id_goal_add
// ADD GOAL
$(document).ready(function(){
    $("#id_goal_add").click(function(){
    	
      if ($("#id_select_absplan").val() != "" && $("#id_select_goal").val() != "Select a goal" ) {
    	
            $.get("add_goal2plan.php?goal="+$("#id_select_goal").val() + "&plan="+$("#id_select_absplan").val(), function(data, status){   
            	
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



// DELETE GOAL
// id_goal_delete
// delete_agent_from_repository.php?agent=7
// verificare 
// detach_goal_from_plan.php?agent=7&plan=3 
$(document).ready(function(){
    $("#id_goal_delete").click(function(){
    	// cancello il goal
    	var goal_to_del = $("#id_select_goal").val();
    	//window.alert("delete_goal_from_repository.php?goal="+goal_to_del);
        $.get("delete_goal_from_repository.php?goal="+goal_to_del, function(data, status){
        	
        
        /*
        $.getJSON("get_plan.php?plan="+$("#id_select_absplan").val(), function(data, status){
        	var plan_goal = data.goal;        
        	if (plan_goal == goal_to_del) {
            	// detach_goal_from_plan.php?goal=3&plan=5            	
            	$.getJSON("detach_goal_from_plan.php?plan="+$("#id_select_absplan").val(), function(data, status){
        			//$("#plans").empty();
        			});
            	}
        });
        */
        
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
        		//cancello la descrizione del goal corrente
        		$("#id_description_goal_corrente").html("___");    
        	}); 		         	            	
		});     	
	});          
});




// ************ PLANS *********************************************************************


// creazione del menu dei piani
// id_select_absplan
// get_plans.php
// [{"id":"1","print":"Piano in cui Amleto ...","name":"Piano 1","type":"Base","timeline":null,"agent":"5","goal":"2","action":"1"},
// "id":"3","print":"Piano di Ofelia per fuggire a quella gabbia di pazzi","name":"Piano 2","type":"Rec","timeline":null,"agent":null,"goal":"2","action":"1"}]

$(document).ready(function(){
   // $("body").load(function(){
        $.getJSON("get_deplans.php", function(data, status){
        	$("#id_select_absplan").empty();
        	var init =  document.createElement("option");
        	$(init).text("select a plan");
            $(init).val("");
            $("#id_select_absplan").append(init);
        	
            $.each(data, function(i, field){
            	//qui scorri l'array del piano per generare testo
            	var s = document.createElement("option");
            	$(s).text(field.name);
            	$(s).val(field.id);
            	$("#id_select_absplan").append(s);
        	})    
        });
    //});
});


// stampa i dati del piano selezionato nei campi per agent, unit, goal
// id_mapping_unit_print
// get_plan.php?plan=3
// {"id":"3","print":"Piano di Ofelia per fuggire a quella gabbia di pazzi","name":"Piano 2","type":"Rec","timeline":null,"agent":null,"goal":"2","action":"1"}

$(document).ready(function(){	
    $("#id_select_absplan").change(function(){ 	
    	
       var plan = $("#id_select_absplan").val();
       $("#id_piano_nascosto").val(plan);
       $.getJSON("get_plan.php?plan="+plan, function(data, status){       
            
            $("#id_description_plan_corrente").text(data.print);
            var agent = data.agent;
            var goal = data.goal;
            var unit = data.timeline;
            var plan = data.id;
            var mapping_i = data.mappingInit;
            var mapping_e = data.mappingEnd;
            
            //window.alert("agent " + agent + " goal " + goal + " unit " + unit + " plan " + plan);
            
            $("#id_plan_corrente").val(plan);
            $("#description_absplan_corrente").html(data.print);
            $("#id_name_absplan_corrente").html(data.name);
            //$("#id_select_absplan").prop('selectedIndex',0);
            //window.alert("id piano: " + plan);
    	
    		//carico agente nella descrizione dell'agente e lo setto come agente corrente  
    		//{"print":"il tranquillo","name":"Bromuro","id":"5"} 
    		$("#id_select_agent").prop('selectedIndex',0); 
    		if (agent != null){
    			$.getJSON("get_agent_info.php?agent="+agent, function(data, status){
    				//scrivo il nome dell'agente
            		$("#id_description_agent_corrente").html(data.print);
            		//setto il menu corrispondente
            		//$("#id_select_agent").val(agent);
            		$("#id_name_agent_corrente").html(data.name);
        		});
        	//non sarebbe necessario se ad ogni selezione di piano si resettasse tutto prima di settare i dati del piano	
    		} else { 	//$("#id_select_agent").prop('selectedIndex',0); 
    					$("#id_name_agent_corrente").html('___'); $("#id_description_agent_corrente").html("___");}        
        	//carico unit
        	/*
        	if (unit != null){	
        		$.getJSON("get_unit.php?unit="+unit, function(data, status){
            		$("#id_campo_mapping_unit").html(data.name);
            		$("#id_description_mapping_unit").html(data.description);
            		$("#id_candidate_mapping").val(unit);
        		});        	
        	} else { 	$("#id_candidate_mapping").prop('selectedIndex',0); 
        				$("#id_description_mapping_unit").html("___");
        				$("#id_campo_mapping_unit").html("___");
        				}
        	*/
        
        	//carico goal
        	$("#id_select_goal").prop('selectedIndex',0);
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
            //carico le units
            //get_actions (plan);
            //nel piano astratto qui abbiamo non le azioni ma i piani figli
            
            $("#candidate_mapping_first").prop('selectedIndex',0);
            if (mapping_i != null) {
            	
            	//$("#candidate_mapping_first").val(mapping_i);
            	//$dati_unit["timeline"] = $id_timeline_unit;
				//$dati_unit["description"] = $description_unit;
				//$dati_unit["name"] = $name_unit;

            	//campo_mapping_first description_mapping_first id_mapping_first (hidden)
            	
            	$.getJSON("get_unit.php?unit="+mapping_i, function(data, status){
            		//$("#candidate_mapping_first").val(mapping_i);
            		
            		$("#campo_mapping_first").html(data.name);
            		$("#description_mapping_first").html(data.description);
            		//$("#id_mapping_first").val(mapping_i);
        		});        	

            	
            	
            	
    			} else {$("#candidate_mapping_first").prop('selectedIndex',0);
    					$("#campo_mapping_first").html("___");
    					$("#description_mapping_first").html("___");
    					$("#id_mapping_first").val();
    					}
            
            $("#candidate_mapping_last").prop('selectedIndex',0);
            if (mapping_e != null) {
            	
            	//$("#candidate_mapping_first").val(mapping_i);
            	//$dati_unit["timeline"] = $id_timeline_unit;
				//$dati_unit["description"] = $description_unit;
				//$dati_unit["name"] = $name_unit;

            	//campo_mapping_first description_mapping_first id_mapping_first (hidden)
            	
            	$.getJSON("get_unit.php?unit="+mapping_e, function(data, status){
            		//$("#candidate_mapping_first").val(mapping_e);
            		//$("#campo_mapping_last").html(data.name);
            		$("#description_mapping_last").html(data.description);
            		//$("#id_mapping_last").val(mapping_i);
        		});        	

            	
            	
            	
    			} else {$("#candidate_mapping_last").prop('selectedIndex',0);
    					$("#campo_mapping_last").html("___");
    					$("#description_mapping_last").html("___");
    					$("#id_mapping_last").val();
    					}
            
            
            //window.alert ("piano corrente:" +  $("#id_select_absplan").val());
            get_subplans(plan);
            
            
            //azzera il box del subplan
            $("#id_id_name").val("")	
			$("#id_print_incident").val("");
			$("#id_agent").html("");
			$("#id_goal").html("");
			$("#id_mapping_init").html("");
			$("#id_mapping_end").html("");	
			$("#id_flag_accomplished").html("");
			$("#cur_subplan").html("");
			
			//azzero precondizioni e effetti
			document.getElementById("id_state_p_no_write_pre").value = ""; 
			// NON CANCELLARE MENU TYPE E STATUS!
			$("#type_state_p_pre").prop('selectedIndex',0);
			$("#status_state_p_pre").prop('selectedIndex',0);
			document.getElementById("print_state_p_pre").value = "";    
            document.getElementById("rec_level_state_p_pre").innerHTML = '-0';    
            document.getElementById("state_name_pre").innerHTML = ""; 
			
			document.getElementById("id_state_p_no_write_eff").value = ""; 
			$("#type_state_p_eff").prop('selectedIndex',0);
			$("#status_state_p_eff").prop('selectedIndex',0);			
			document.getElementById("print_state_p_eff").value = "";    
            document.getElementById("rec_level_state_p_eff").innerHTML = '-0';    
            document.getElementById("state_name_eff").innerHTML = ""; 

            
            //carico precondizioni e effetti
            get_states(plan);
            
            
            
       });                       
    });
});


// creazione di un piano
// id_button_create_plan
// id_create_plan id_print_create_plan
// create_plan.php?name=Piano+prova&print=guarda+che+bello&type=Base


$(document).ready(function(){
    $("#id_button_create_absplan").click(function(){
    	
     if ($("#id_create_absplan").val() != "insert new abstract plan name") {	
    	//window.alert("create_plan.php?name="+$("#id_create_plan").val()+"&print="+$("#id_print_create_plan").val()+"&type=Base");
    	//window.alert("create_absplan.php?name="+$("#id_create_absplan").val()+"&print="+$("#print_create_absplan").val()+"&type=Rec");
		$.get("create_absplan.php?name="+$("#id_create_absplan").val()+"&print="+$("#print_create_absplan").val()+"&type=Rec"+"&accomplished="+$("#id_accomplished").val(), function(data, success){
    			
    			//location.reload();
    			
    			
    			
    			var nuovo_id = parseInt(data);
 
				$.getJSON("get_plan.php?plan="+nuovo_id, function(data, status){
				// visualizzo i dati
	
        		// mettere name e description al loro posto
        		$("#id_name_absplan_corrente").html(data.name);
				$("#description_absplan_corrente").html(data.print);
				
				});
    			
    			$.getJSON("get_deplans.php", function(data, status){
    				
    			
        		$("#id_select_absplan").empty();
        		
        		var init =  document.createElement("option");
        		$(init).text("select a plan");
            	$(init).val("");
            	$("#id_select_absplan").append(init);
				var my_index = 0;

            		$.each(data, function(i, field){
            		//qui scorri l'array del piano per generare testo
            		//window.alert("passo di qui");

            		var s = document.createElement("option");
            		$(s).text(field.name);
            		$(s).val(field.id);
            		//window.alert (field.id);
            		$("#id_select_absplan").append(s);
            		my_index++;
            		
            			if (field.id == nuovo_id) {nuovo_index = my_index;} 
            				            		
        			})
        		
        		// settare la select sul piano appena creato          		 	
        		$('#id_select_absplan').change();
        		
        		$('#id_select_absplan').val(nuovo_id); 

        		
        		
        		$("#id_select_agent").prop('selectedIndex',0);
        		$("#id_select_goal").prop('selectedIndex',0);
        	    $("#id_mapping_init").prop('selectedIndex',0);
        	    $("#id_mapping_end").prop('selectedIndex',0);     
        	    
        	    
        	    //AZZERARE A MANO TUTTI I CAMPI 
        	    $("#id_name_agent_corrente").html("____");
				$("#id_description_agent_corrente").html("____");
				$("#campo_mapping_first").html("____");
				$("#description_mapping_first").html("____");
				$("#campo_mapping_last").html("____");
				$("#description_mapping_last").html("____");
				$("#id_name_goal").html("____");
				$("#id_description_goal_corrente").html("____");
        	    
        	    
        	    //azzera il box del subplan
            	$("#id_id_name").val("")	
				$("#id_print_incident").val("");
				$("#id_agent").html("");
				$("#id_goal").html("");
				$("#id_mapping_init").html("");
				$("#id_mapping_end").html("");	
				$("#id_flag_accomplished").html("");
				$("#cur_subplan").html("");
        	    
        	    // se è appena creato non avrà precodizioni e effetti
        	    get_subplans(nuovo_id);
            	//carico precondizioni e effetti
            	get_states(nuovo_id);        						    
        		});        		      		        		   		
 		});	
      }	else {window.alert ("Please insert a plan name");}
    }); 
});


//id_button_delete_plan
$(document).ready(function(){
    $("#id_button_delete_plan").click(function(){
    	//window.alert("delete_plan_from_repository.php?plan="+$("#id_select_absplan").val());
		$.get("delete_plan_from_repository.php?plan="+$("#id_select_absplan").val(), function(data, success){
			location.reload();       
			/*			
			// genero da capo il menu dei piani
			$.getJSON("get_plans.php", function(data, status){
        	$("#id_select_absplan").empty();
        	var init =  document.createElement("option");
        	$(init).text("select a plan");
            $(init).val("");
            $("#id_select_absplan").append(init);       	
            $.each(data, function(i, field){
            	//qui scorri l'array del piano per generare testo
            	var s = document.createElement("option");
            	$(s).text(field.name);
            	$(s).val(field.id);
            	$("#id_select_absplan").append(s);
        	})    
        });
		$("#id_name_absplan_corrente").html("____");
		$("#description_absplan_corrente").html("____");	
		//AZZERARE A MANO TUTTI I CAMPI 
        	    $("#id_name_agent_corrente").html("____");
				$("#id_description_agent_corrente").html("____");
				$("#campo_mapping_first").html("____");
				$("#description_mapping_first").html("____");
				$("#campo_mapping_last").html("____");
				$("#description_mapping_last").html("____");
				$("#id_name_goal").html("____");
				$("#id_description_goal_corrente").html("____");		
		//azzera il box del subplan
            	$("#id_id_name").val("")	
				$("#id_print_incident").val("");
				$("#id_agent").html("");
				$("#id_goal").html("");
				$("#id_mapping_init").html("");
				$("#id_mapping_end").html("");	
				$("#id_flag_accomplished").html("");
				$("#cur_subplan").html("");
        // se è appena creato non avrà precodizioni e effetti
        	    //get_subplans(0);
        	    removeOptions(document.getElementById("id_curr_subplans"));
            	removeOptions(document.getElementById("id_cand_subplan"));
            	removeOptions(document.getElementById("id_dopo"));
            	var s1 = document.getElementById("id_curr_subplans");
            	var s2 = document.getElementById("id_cand_subplan");
            	var s3 = document.getElementById("id_dopo");
            	var istr1 = document.createElement("option");
            	var istr2 = document.createElement("option");
            	var istr3 = document.createElement("option");
            	var istr4 = document.createElement("option");
            	istr1.text = "select a subplan";
            	istr2.text = "select a candidate subplan";
            	istr3.text = "inserisci dopo ...";
            	istr3.value = "missing";
            	istr4.text = "inserisci per primo";
            	istr4.value = "primo";
            	s1.add(istr1);
            	s2.add(istr2);
            	s3.add(istr3);
            	s3.add(istr4);     
            	//carico precondizioni e effetti
            	get_states(0);       	    
		*/
		});
    });
});



// cancellazione piano
// si cancella il piano in focus
// $id_plan = $_REQUEST["plan"];
// id_delete_plan
// //delete_plan_from_repository.php?plan=7
$(document).ready(function(){
    $("#id_delete_plan").click(function(){
    	// cancello il piano
    	var plan_to_del = $("#id_select_absplan").val();
    	//alert("delete_plan_from_repository.php?plan="+plan_to_del);
        $.getJSON("delete_plan_from_repository.php?plan="+plan_to_del, function(data, status){
                
        });	
 		$.getJSON("get_deplans.php", function(data, status){				
    			
        		$("#id_select_absplan").empty();
            		$.each(data, function(i, field){
            		//qui scorri l'array del piano per generare testo
            		//window.alert("passo di qui");

            		var s = document.createElement("option");
            		$(s).text(field.name);
            		$(s).val(field.id);
            		$("#id_select_absplan").append(s);
        			})
        					    
        		});     
  	});
});




// ******************* CHILDREN PLANS *******************************

// id="cand_subplan"
// id="id_curr_subplans" 

function get_subplans (plan) {
	
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
            	var a = my_response.children;
            	var ca = my_response.candidate;
            	var dopo = a;
           
            	
            	
            	removeOptions(document.getElementById("id_curr_subplans"));
            	removeOptions(document.getElementById("id_cand_subplan"));
            	removeOptions(document.getElementById("id_dopo"));
            	
            	var s1 = document.getElementById("id_curr_subplans");
            	var s2 = document.getElementById("id_cand_subplan");
            	var s3 = document.getElementById("id_dopo");
            	
            	var istr1 = document.createElement("option");
            	var istr2 = document.createElement("option");
            	var istr3 = document.createElement("option");
            	var istr4 = document.createElement("option");
            	istr1.text = "select a subplan";
            	istr2.text = "select a candidate subplan";
            	istr3.text = "inserisci dopo ...";
            	istr3.value = "missing";
            	istr4.text = "inserisci per primo";
            	istr4.value = "primo";
            	s1.add(istr1);
            	s2.add(istr2);
            	s3.add(istr3);
            	s3.add(istr4);
            	
            	
            	
            	//{"id":"38","print":"no need to argue","name":"piano B","type":"Base","timeline":null,"agent":null,"goal":null,"action":null}
            	
            	
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
            	
            	ca.forEach(function(entry) {
            		
            		//window.alert ("candidate subplan");
    				var id = entry.id;
    				var printPlan = entry.name;
    				var option = document.createElement("option");
					option.text = printPlan;
					option.value = id;
					s2.add(option);
					});
					
				
				dopo.forEach(function(entry) {
            		
            		//window.alert ("current subplan");
    				var id = entry.id;
    				var printPlan = entry.name;
    				var option = document.createElement("option");
					option.text = printPlan;
					option.value = id;
					s3.add(option);
					});	
            	
	
				
				
	 		}
        }
        xmlhttp.open("GET","get_children_plans.php?plan="+plan,false);
        xmlhttp.send();		
}




/*
id="cand_subplan"
id="curr_subplans"
id="id_dopo"
id="id_button_addsubplan"
*/

$(document).ready(function(){
    $("#id_button_addsubplan").click(function(){   	
    	// aggiungo il piano
    	var father_id = $("#id_select_absplan").val();
		var child_id = $("#id_cand_subplan").val();
		var reference_id = $("#id_dopo").val();
		
		if (reference_id != "missing") { 
		
        	$.get("add_subplan.php?father="+father_id+"&"+"child="+child_id+"&reference="+reference_id, function(data, status){
           	//window.alert("inserito " + child_id + " dopo " + reference_id);
           	get_subplans(father_id);       
        	});
		} else {window.alert("devi selezionare una voce del menu \"inserisci dopo ...\"!")}           					    
     });     
});


// d="id_button_detachsubplan"
$(document).ready(function(){
    $("#id_button_detachsubplan").click(function(){
    	// cancello il piano
    	var father_id = $("#id_select_absplan").val();
		var child_id = $("#id_curr_subplans").val();
    	//window.alert("delete_goal_from_repository.php?goal="+goal_to_del);
    	//alert("delete_plan_from_repository.php?plan="+plan_to_del);
        $.get("detach_subplan_from_plan.php?father="+father_id+"&"+"child="+child_id, function(data, status){
           //window.alert("inserito " + child_id + " dopo " + reference_id);
           get_subplans(father_id);       
        });           					    
     });     
});


// {"id":"3","print":"Piano di Ofelia per fuggire a quella gabbia di pazzi","name":"Piano 2","type":"Rec","timeline":"3","agent":"34","goal":null,"action":"1508","mappingInit":"2","mappingEnd":null}
// mostro il piano 
$(document).ready(function(){	
    $("#id_cand_subplan").change(function(){
    	
    	$("#id_curr_subplans").prop('selectedIndex',0);
    
    	
    	var plan = $("#id_cand_subplan").val();
    	$.getJSON("get_plan_verbose.php?plan="+plan, function(data, status){
		// visualizzo i dati

		
            var agent = data.agent;
            var goal = data.goal;
            var unit = data.timeline;
            var plan = data.id;
			/*
			id="id_id_name"
			id="id_print_incident"
			id="id_agent
			id="id_goal"
			id="id_accomplished"
			id="id_mapping_init"
			id="id_mapping_end"
			 $("#id_description_goal_corrente").html(data.print);
			*/
			$("#id_id_name").val(data.name);
			
			$("#id_print_incident").val(data.print);
			
			//GET DI AGENT
			$("#id_agent").html(data.agent);
			
			//GET DI GOAL
			$("#id_goal").html(data.goal);
			//$("#id_accomplished").html(data.goal);
			
			//GET DI MAPPING INIT E END
			$("#id_mapping_init").html(data.mappingInit);
			$("#id_mapping_end").html(data.mappingEnd);
			
			$("#id_flag_accomplished").html(data.accomplished);
			
			$("#cur_subplan").html(data.name);
			
		});
      
     });     
});


$(document).ready(function(){	
    $("#id_curr_subplans").change(function(){
    	
    	$("#id_cand_subplan").prop('selectedIndex',0);
    	
    	var plan = $("#id_curr_subplans").val();
    	$.getJSON("get_plan_verbose.php?plan="+plan, function(data, status){
		// visualizzo i dati

		
            var agent = data.agent;
            var goal = data.goal;
            var unit = data.timeline;
            var plan = data.id;
			/*
			id="id_id_name"
			id="id_print_incident"
			id="id_agent
			id="id_goal"
			id="id_accomplished"
			id="id_mapping_init"
			id="id_mapping_end"
			 $("#id_description_goal_corrente").html(data.print);
			*/
			$("#id_id_name").val(data.name);
			$("#id_print_incident").val(data.print);
			$("#id_agent").html(data.agent);
			$("#id_goal").html(data.goal);
			//$("#id_accomplished").html(data.goal);
			$("#id_mapping_init").html(data.mappingInit);
			$("#id_mapping_end").html(data.mappingEnd);
			$("#id_flag_accomplished").html(data. accomplished);
			
			$("#cur_subplan").html(data.name);

			
		});
      
     });     
});



/*
function addSubplan (father, child, reference){
		//if (document.getElementById('check').value != "no"){
		//document.getElementById('test').innerHTML = "hai selezionato: " + unit;	
		//window.alert("");	
		var father_id = $("#cand_subplan").val();
		var child_id = $("#cand_subplan").val();
	
		
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
            	           	
            	removeOptions(document.getElementById("curr_incident"));
            	removeOptions(document.getElementById("cand_incident"));
            	
            	get_actions(plan);
                                    
            }
        }
        //add_action2plan.php?action=5&plan=5
        xmlhttp.open("GET","add_subplan.php?father="+father+"&"+"child="+child+"&reference="+reference,false);
        //xmlhttp.open("GET","add_action.php?unit="+unit+"&"+"action="+action+"&"+"print="+print,false);
        xmlhttp.send();	
	//}    
	
}
*/




// ****************** AZIONI **************************************************

function get_actions (unit) {
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
            	var a = my_response.actions;
            	var ca = my_response.candidate;
            	
            	removeOptions(document.getElementById("curr_incident"));
            	removeOptions(document.getElementById("cand_incident"));
            	
            	var s1 = document.getElementById("curr_incident");
            	var s2 = document.getElementById("cand_incident");
            	
            	var istr1 = document.createElement("option");
            	var istr2 = document.createElement("option");
            	istr1.text = "select an action";
            	istr2.text = "select an action";
            	s1.add(istr1);
            	s2.add(istr2);
            	
            	a.forEach(function(entry) {
    				var id = entry.id;
    				var printAction = entry.print;
    				var option = document.createElement("option");
					option.text = printAction;
					option.value = id;
					s1.add(option);
					});	
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
            	           	
            	removeOptions(document.getElementById("curr_incident"));
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

// addAction(document.incidents.add_incident.value,id_unit_corrente,document.incidents.add_incident.text)
// document.incidents.id_incident.value,document.incidents.print_incident.value, id_unit_corrente, id_timeline_corrente
function createAction (action, print, plan) {
		//window.alert("create_action.php?action="+action+"&"+"print="+print);
	
		//if ((action != "") && (unit !="")){
	
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
            	
			}
        }
        xmlhttp.open("GET","create_action.php?action="+action+"&"+"print="+print,false);
        xmlhttp.send();	    
	//}   else {window.alert ("devi inserire nome e descrizione della unit");}	            	
}      


//add_state.php?state=3&timeline=4&flag=eff
function deleteAction (plan,action){
	//if (document.getElementById('check').value != "no"){
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
            	
            	removeOptions(document.getElementById("curr_incident"));
            	removeOptions(document.getElementById("cand_incident"));
            	
            	get_actions(plan);
                                    
            }
        }
        //detach_agent_from_plan.php?action=7&plan=3
        xmlhttp.open("GET","detach_action_from_plan.php?plan="+plan+"&"+"action="+action,false);
        xmlhttp.send();	
	//}
}


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
                
                document.getElementById("id_hidden_action").value = my_response.id;
                //document.getElementById('test').innerHTML = response;
                document.getElementById("id_id_incident").value = my_response.name; 
                //document.getElementById("set_state_p_"+flag).innerHTML = ... ;
                document.getElementById("id_print_incident").value = my_response.print;             
                document.getElementById('set_incident').innerHTML  = my_response.nameunit;
                
            }
        }
        xmlhttp.open("GET","get_action_features.php?action="+action,false);
        xmlhttp.send();	
	
}



function removeOptions(selectbox)
{
    var i;
    for(i=selectbox.options.length-1;i>=0;i--)
    {
        selectbox.remove(i);
    }
}




// ********************* STATES *************************************************************

$(document).ready(function(){	
    $("#precondizioni_candidate").change(function(){

	 	var state = $("#precondizioni_candidate").val();
		
		getInfoState(state,'pre'); 
		$("#precondizioni_unit").prop('selectedIndex',0);


    });
})

$(document).ready(function(){	
    $("#precondizioni_unit").change(function(){

	 	var state = $("#precondizioni_unit").val();
		
		getInfoState(state,'pre'); 
		$("#precondizioni_candidate").prop('selectedIndex',0);


    });
})


$(document).ready(function(){	
    $("#effetti_candidati").change(function(){

	 	var state = $("#effetti_candidati").val();
		
		getInfoState(state,'eff'); 
		$("#effetti_unit").prop('selectedIndex',0);


    });
})

$(document).ready(function(){	
    $("#effetti_unit").change(function(){

	 	var state = $("#effetti_unit").val();
		
		getInfoState(state,'eff'); 
		$("#effetti_candidati").prop('selectedIndex',0);


    });
})


function get_states(unit) {	
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
            	//window.alert ("get states 1");
            	
            	var my_response = JSON.parse(xmlhttp.responseText);        	
            	var s1 = document.getElementById("precondizioni_unit");
            	var s2 = document.getElementById("effetti_unit");
            	var s3 = document.getElementById("precondizioni_candidate");
            	var s4 = document.getElementById("effetti_candidati");       
            	
            	
            	removeOptions(s1);
            	removeOptions(s2);
            	removeOptions(s3);
            	removeOptions(s4);
            	
            	
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
        xmlhttp.open("GET","get_states.php?plan="+unit,false);
        xmlhttp.send();	
}


function addState (state, plan, flag){
	
		//if (document.getElementById('check').value != "no"){	
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
            	           	
            		/*
            		removeOptions(document.getElementById("precondizioni_unit"));
            		removeOptions(document.getElementById("effetti_unit"));
            		removeOptions(document.getElementById("precondizioni_candidate"));
            		removeOptions(document.getElementById("effetti_candidati")); 
            		*/
            	
            		get_states(plan);
                                    
            	}
        	}
        	//add_state2plan.php?state=1700&plan=3&flag=pre
        	xmlhttp.open("GET","add_state2plan.php?state="+state+"&"+"plan="+plan+"&"+"flag="+flag,false);
        	xmlhttp.send();	
		//}	
}


//add_state.php?state=3&timeline=4&flag=eff
function deleteState (state, plan, flag){
	//if (document.getElementById('check').value != "no"){
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
            	
            	document.getElementById("id_p_viewer").reset();
				document.getElementById("id_e_viewer").reset();
            	        	
            	/*
            	removeOptions(document.getElementById("precondizioni_unit"));
            	removeOptions(document.getElementById("effetti_unit"));
            	removeOptions(document.getElementById("precondizioni_candidate"));
            	removeOptions(document.getElementById("effetti_candidati"));    
            	*/        	
            	
            	get_states(plan);
                                    
            }
        }
        //detach_state_from_plan.php?plan=3&state=1700&flag=eff
        xmlhttp.open("GET","detach_state_from_plan.php?state="+state+"&"+"plan="+plan+"&"+"flag="+flag,false);
        xmlhttp.send();	
	//}
}

function getInfoState (state, flag){
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
            	//{"id":"5783","print":"arci stufa ","type":"BEL","status":"1","description":"stufissima"}
            	
            	
            	var my_response = JSON.parse(xmlhttp.responseText);
                
                //document.getElementById('test').innerHTML = response;
                
                
                //document.getElementById("id_state_p_"+flag).innerHTML = my_response.id; 
                document.getElementById("id_state_p_no_write_"+flag).value = my_response.description;
                //document.getElementById("set_state_p_"+flag).innerHTML = my_response.set;
                // cambiare
                
                var menu = document.getElementById("type_state_p_"+flag); 
                //window.alert("id del menu " + "type_state_p_"+flag);
                
                
                var opts = menu.options;
    				for (var opt, j = 0; opt = opts[j]; j++) {
    					//window.alert("passo da qui");
    					
        				if(opt.value == my_response.type) {
            				menu.selectedIndex = j;
            				//window.alert("passo da qui");
            			break;
        				}
    			}
                
                var menu = document.getElementById("status_state_p_"+flag);
                var opts = menu.options;
    				for(var opt, j = 0; opt = opts[j]; j++) {
        				if(opt.value == my_response.status) {
            				menu.selectedIndex = j;
            			break;
        				}
    			}
                //document.getElementById("type_state_p_"+flag).value = my_response.type;
                //document.getElementById("status_state_p_"+flag).value = my_response.status;
                document.getElementById("print_state_p_"+flag).value = my_response.print;
                
                document.getElementById("rec_level_state_p_"+flag).innerHTML = '-0';  
                
                document.getElementById("state_name_"+flag).innerHTML = my_response.description; 
                
                //State name <span id="state_name_pre"></span>   
                         
                
            }
        }
        xmlhttp.open("GET","get_state_features.php?state="+state,false);
        xmlhttp.send();		
}


function createState (id,type,status,myprint,plan,flag){
	//if (document.getElementById('check').value != "no"){
		
		if ((id != '') && (myprint != '')) {
		
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
            	
            	//mi restituisco l'id dello stato creato e lo uso per fare la add (parametro state)
            	
            	removeOptions(document.getElementById("precondizioni_unit"));
            	removeOptions(document.getElementById("effetti_unit"));
            	removeOptions(document.getElementById("precondizioni_candidate"));
            	removeOptions(document.getElementById("effetti_candidati")); 
            	
                addState (response, plan);
            	//addState(document.p_viewer.id_state_pre.value, id_timeline_corrente, 'pre', id_unit_corrente);
            	
            	//get_states(unit, timeline);
                                    
            }
        }
        xmlhttp.open("GET","create_state.php?id="+id+"&"+"type="+type+"&"+"print="+myprint+"&"+"status="+status,false);
        xmlhttp.send();	
	} else {window.alert ("devi inserire nome e descrizione dello stato");}
 // }
}

function deleteStateFromRep (state, plan) {
		//document.getElementById('test').innerHTML = "hai selezionato: " + candidate_unit;		
		//if (state != "select a state"){
		
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
            	
            	
            	removeOptions(document.getElementById("precondizioni_unit"));
            	removeOptions(document.getElementById("effetti_unit"));
            	removeOptions(document.getElementById("precondizioni_candidate"));
            	removeOptions(document.getElementById("effetti_candidati"));       
            	     	
            	
            	get_states(plan);
            	
			}
        }
        xmlhttp.open("GET","delete_state_from_repository.php?state="+state,false);
        xmlhttp.send();		
        
		//} else {window.alert ("devi selezionare uno stato candidato per poterlo cancellare");}            	
}     


function deleteActionFromRep (action, plan) {
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
            	//get_actions(unit);
            	
            	removeOptions(document.getElementById("curr_incident"));
            	removeOptions(document.getElementById("cand_incident"));
            	
            	get_actions(plan);
            	
            	//document.getElementById("id_hidden_action").value = my_response.id;
                //document.getElementById('test').innerHTML = response;
                document.getElementById("id_id_incident").value = ''; 
                //document.getElementById("set_state_p_"+flag).innerHTML = ... ;
                document.getElementById("id_print_incident").value = '';             
                document.getElementById('set_incident').innerHTML  = '___';
            	
			}
        }
        xmlhttp.open("GET","delete_action_from_repository.php?action="+action,false);
        xmlhttp.send();		            	
}       


// **********************************************************************************
// *********** GESTIONE PAGINA ******************************************************
// **********************************************************************************







function clear_pagina (){
	
	
	//unit_content_form --> precondizioni
	//p-viewer
	//e-viewer
	//document.getElementById('test').innerHTML = "CUCU"; 
	document.getElementById("id_p_viewer").reset();
	document.getElementById("id_e_viewer").reset();
	document.getElementById("form_incidents").reset();
	removeOptions(document.getElementById("precondizioni_unit"));
    removeOptions(document.getElementById("effetti_unit"));
    removeOptions(document.getElementById("precondizioni_candidate"));
    removeOptions(document.getElementById("effetti_candidati"));  
    // aggiunti
    removeOptions(document.getElementById("candidate_previous"));
	removeOptions(document.getElementById("candidate_next")); 
	removeOptions(document.getElementById("curr_incident"));
    removeOptions(document.getElementById("cand_incident"));
    
    document.getElementById('campo_unit_precedente').innerHTML = '_____';
	document.getElementById('description_unit_precedente').innerHTML = '_____';
	document.getElementById('campo_unit_seguente').innerHTML = '_____';
	document.getElementById('description_unit_seguente').innerHTML = '_____';
    
    //inizializzo i menu con una opzione generica
    /*
    var istr1 = document.createElement("option"); 
    var istr2 = document.createElement("option"); 
    var istr3 = document.createElement("option"); 
    var istr4 = document.createElement("option"); 
    istr1.text = "scegli uno stato";
    istr2.text = "scegli uno stato";
    istr3.text = "scegli uno stato";
    istr4.text = "scegli uno stato";
    document.getElementById("precondizioni_unit").add(istr1);
    document.getElementById("effetti_unit").add(istr2);
    document.getElementById("precondizioni_candidate").add(istr3);
    document.getElementById("effetti_candidati").add(istr4);
    */
	
}

	




