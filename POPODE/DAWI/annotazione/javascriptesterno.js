

function get_all (unit, timeline) {
	
	if (unit == "select a unit to edit") {location.reload();} else {
	
		if (document.getElementById('check').value != "no") {
	
		get_units(unit, timeline);
		get_candidate_units(unit,timeline);
		get_states(unit,timeline);
		get_actions(unit);
		//get_units_select();
		}
	}
}
	
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
            	//document.getElementById('test').innerHTML = response; 
            	
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

/*
{"preconditions":{"4":{"id":"4","print":"Ofelia pensa che Amleto sia un cretino","type":"BEL","status":"1"},"6":{"id":"6","print":"Bromuro \u00e8 andato a casa sua","type":"SOA","status":"0"},"2":{"id":"2","print":"Ofelia non crede pi\u00f9 nell'amicizia tra uomo e donna","type":"BEL","status":"1"}},"effects":{"5":{"id":"5","print":"Amleto sa conosce la verit\u00e0","type":"SOA","status":"1"}},"candidate_preconditions":{...}, "candidate_effects":{...}}
*/


function resetPagina(){
	
	
	var istr1 = document.createElement("option"); 
    var istr2 = document.createElement("option"); 
    var istr3 = document.createElement("option"); 
    var istr4 = document.createElement("option"); 
    
    istr1.text = "select a state";
    istr2.text = "select a state";
    istr3.text = "select a state";
    istr4.text = "select a state";
    document.getElementById("precondizioni_unit").add(istr1);
    document.getElementById("effetti_unit").add(istr2);
    document.getElementById("precondizioni_candidate").add(istr3);
    document.getElementById("effetti_candidati").add(istr4);
    
    var s1 = document.getElementById("curr_incident");
    var s2 = document.getElementById("cand_incident");
            	
    var istr1 = document.createElement("option");
    var istr2 = document.createElement("option");
    istr1.text = "select an action";
    istr2.text = "select an action";
    s1.add(istr1);
    s2.add(istr2);
    
    var istr5 = document.createElement("option"); 
    var istr6 = document.createElement("option"); 
	istr5.text = "select previous unit";
    istr6.text = "select previous unit";

	document.getElementById("candidate_previous").add(istr5);
    document.getElementById("candidate_next").add(istr6);
	
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



function get_actions (unit) {
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
        xmlhttp.open("GET","get_actions_unit.php?unit="+unit,false);
        xmlhttp.send();		
}	

function addAction (action, unit, print){
		if (document.getElementById('check').value != "no"){
		//document.getElementById('test').innerHTML = "hai selezionato: " + unit;		
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
            	
            	get_actions(unit);
                                    
            }
        }
        xmlhttp.open("GET","add_action.php?unit="+unit+"&"+"action="+action+"&"+"print="+print,false);
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
            	
            	
            	removeOptions(document.getElementById("curr_incident"));
            	removeOptions(document.getElementById("cand_incident"));
            	
            	//window.alert("passo di qui");
                document.getElementById("cur_incident").innerHTML = ""; 
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
                
                //document.getElementById('test').innerHTML = response;
                document.getElementById("cur_incident").innerHTML = my_response.name; 
                //document.getElementById("set_state_p_"+flag).innerHTML = ... ;
                document.getElementById("id_print_incident").value = my_response.print;             
                //document.getElementById('set_incident').innerHTML  = my_response.nameunit;
                document.getElementById('name_incident').value  = my_response.name;
            }
        }
        xmlhttp.open("GET","get_action_features.php?action="+action,false);
        xmlhttp.send();	
	
}

//{"id":"2","print":"Ofelia non crede pi\u00f9 nell'amicizia tra uomo e donna","set":"CSS 13","type":"BEL","status":"1"}
function getInfoState (state, timeline, flag){
	
		
		//window.alert("getInfoState su stato: " + state + " - timeline: " + timeline + " - flag: " + flag);
		//window.alert("get_state_features.php?state="+state+"&"+"timeline="+timeline+"&"+"flag="+flag);
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
            	
            	//window.alert("risposta: " + response);
                
                //document.getElementById('test').innerHTML = response;
                
                
                //document.getElementById("id_state_p_"+flag).innerHTML = my_response.id; 
                
                //document.getElementById("id_state_p_no_write_"+flag).value = my_response.description;
                document.getElementById("id_state_p_no_write_"+flag).value = my_response.description;
               // document.getElementById("set_state_p_"+flag).innerHTML = my_response.set;
               document.getElementById("state_name_"+flag).innerHTML = my_response.description;  

                
                
                
                // cambiare
                var menu = document.getElementById("type_state_p_"+flag);
                var opts = menu.options;
    				for(var opt, j = 0; opt = opts[j]; j++) {
        				if(opt.value == my_response.type) {
            				menu.selectedIndex = j;
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
                         
                
            }
        }
        xmlhttp.open("GET","get_state_features_timeline.php?state="+state+"&"+"timeline="+timeline+"&"+"flag="+flag,false);
        xmlhttp.send();		
}


function getInfoSolitaryState (state, timeline, flag){
		
		//window.alert("getInfoState su stato: " + state + " - timeline: " + timeline + " - flag: " + flag);
		//window.alert("get_state_features.php?state="+state+"&"+"timeline="+timeline+"&"+"flag="+flag);
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
            	
            	//window.alert("risposta: " + response);
                
                //document.getElementById('test').innerHTML = response;
                
                
                //document.getElementById("id_state_p_"+flag).innerHTML = my_response.id; 
                document.getElementById("id_state_p_no_write_"+flag).value = my_response.description;
               // document.getElementById("set_state_p_"+flag).innerHTML = my_response.set;
                
                
                
                // cambiare
                var menu = document.getElementById("type_state_p_"+flag);
                var opts = menu.options;
    				for(var opt, j = 0; opt = opts[j]; j++) {
        				if(opt.value == my_response.type) {
            				menu.selectedIndex = j;
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
                //state_name_pre
                document.getElementById("state_name_"+flag).innerHTML = my_response.description;  
                
                
                document.getElementById("print_state_p_"+flag).value = my_response.print;
                document.getElementById("rec_level_state_p_"+flag).innerHTML = '-0';     
                         
                
            }
        }
        xmlhttp.open("GET","get_solitary_state_features.php?state="+state+"&"+"flag="+flag,false);
        xmlhttp.send();		
}





function setRightState (unit) {	
	var menu = document.getElementById("id_select_unit");
                var opts = menu.options;
    				for(var opt, j = 0; opt = opts[j]; j++) {
        				if(opt.value == unit) {
            				menu.selectedIndex = j;
            			break;
        				}
    			}	
}

//add_state.php?state=3&timeline=4&flag=eff

function addState (state, timeline, flag, unit){
	
		if (document.getElementById('check').value != "no"){	
			if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            //window.alert("state value: " + state + " - timeline vale: " + timeline + " - flag vale: " + flag);
            
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
            	
            		//window.alert("get_states: unit " + unit + " - timeline: " + timeline);
            		get_states(unit, timeline);
                                    
            	}
        	}
        	xmlhttp.open("GET","add_state.php?state="+state+"&"+"timeline="+timeline+"&"+"flag="+flag,false);
        	xmlhttp.send();	
		}	
}


function removeOptions(selectbox)
{
    var i;
    for(i=selectbox.options.length-1;i>=0;i--)
    {
        selectbox.remove(i);
    }
}

//add_state.php?state=3&timeline=4&flag=eff
function deleteState (state, timeline, flag, unit){
	if (document.getElementById('check').value != "no"){
		//document.getElementById('test').innerHTML = "hai selezionato: " + candidate_unit;	
		
		if (state == "select a state"){window.alert("please select a state to be removed from the unit");} else {
			
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
            	        	
            		removeOptions(document.getElementById("precondizioni_unit"));
            		removeOptions(document.getElementById("effetti_unit"));
            		removeOptions(document.getElementById("precondizioni_candidate"));
            		removeOptions(document.getElementById("effetti_candidati"));            	
            	
            		get_states(unit, timeline);
                                    
            	}
        	}
        	xmlhttp.open("GET","delete_state.php?state="+state+"&"+"timeline="+timeline+"&"+"flag="+flag,false);
        	xmlhttp.send();	
		}	
  	}
}

// creates and adds a state to the unit's preconditions or effects
// TOGLIERE L'AUTOINCREMENT DAL CAMPO ID DELLO STATO ALTRIMENTI NON LEGGE IL VALORE INSERITO DALL'UTENTE
// ci vuole il controllo che lo stato non esista già

function createState (id,type,status,myprint,unit,timeline,flag){
	
	var timeline = $('#id_timeline_corrente').val();
	
	//window.alert("create_state.php?id="+id+"&"+"type="+type+"&"+"print="+myprint+"&"+"status="+status);
	
	if (document.getElementById('check').value != "no"){
		
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
            	
            	
            	//ADD NON FUNZIONA BENE
            	//addState (response, timeline, flag, unit);
            	//addState(document.p_viewer.id_state_pre.value, id_timeline_corrente, 'pre', id_unit_corrente);
            	
            	get_states(unit, timeline);
                                    
            }
        }
        xmlhttp.open("GET","create_state.php?id="+id+"&"+"type="+type+"&"+"print="+myprint+"&"+"status="+status,false);
        xmlhttp.send();	
	} else {window.alert ("devi inserire nome e descrizione dello stato");}
  }
}

	


// [{"id":"1","print":"Unit1"},{"id":"2","print":"Unit2"},{"id":"3","print":"Unit3"},{"id":"4","print":"Unit4"},{"id":"5","print":"Unit 5"}]
function get_units_select () {
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
					option.text = printAction;
					option.value = id;
					sel.add(option);
					});	
					         					
	 		}
        }
        xmlhttp.open("GET","get_candidate_units.php",false);
        xmlhttp.send();		
}	


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
            	
            	//document.getElementById('test').innerHTML = "timeline: " + my_response.timeline +  " description: " + my_response.description + " - unit: " + unit; 
            	document.getElementById('id_name_unit_corrente').innerHTML = my_response.name;  
            	document.getElementById('description_unit_corrente').innerHTML = my_response.description;  
            	document.getElementById('id_timeline_corrente').value = my_response.timeline;
            	document.getElementById('id_unit_corrente').value = unit;
            	document.getElementById('check').value = "ok";
            	
			}
        }
        xmlhttp.open("GET","get_timeline.php?unit="+unit,false);
        xmlhttp.send();	
	}        	            	
}            	
            	
            	

function createUnit (unit, description) {
	
	if (((unit != "") && (description != "")) && ((unit != "nome nuova unit") && (description != "descrizione nuova unit"))){	
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
            	document.getElementById('id_unit_corrente').value = response;
            	document.getElementById('check').value = "ok";
            	//window.alert("creata la unit con id" + response);
     
            	
			}
        }
        xmlhttp.open("GET","createUnit.php?print="+unit+"&"+"description="+description,false);
        xmlhttp.send();	
	} else {
			window.alert ("devi inserire un nome e una descrizione per la unit"); 
			document.getElementById('check').value = "no";
			}    	            	
}            	
   
   
function deleteUnit (unit, timeline) {
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
            	//document.getElementById('id_unit_corrente').value = response;	
 
 				            	
            	//id_name_unit_corrente
				//description_unit_corrente
            	
            	document.getElementById('id_name_unit_corrente').innerHTML = '___';  
            	document.getElementById('description_unit_corrente').innerHTML = '___';  

            	
            	document.getElementById('check').value = "no";
            	removeOptions(document.getElementById('id_select_unit'));
			}
        }
        xmlhttp.open("GET","delete_unit.php?unit="+unit+"&"+"timeline="+timeline,false);
        xmlhttp.send();		            	
}     


  

function deleteStateFromRep (state, unit, timeline) {
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
            	
            	
            	removeOptions(document.getElementById("precondizioni_unit"));
            	removeOptions(document.getElementById("effetti_unit"));
            	removeOptions(document.getElementById("precondizioni_candidate"));
            	removeOptions(document.getElementById("effetti_candidati"));            	
            	
            	get_states(unit, timeline);
            	
			}
        }
        xmlhttp.open("GET","delete_state_from_repository.php?state="+state,false);
        xmlhttp.send();		            	
}     


function deleteActionFromRep (action) {
	    //window.alert("delete_action_from_repository.php?action="+action);
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
            	
            	
            	//window.alert("passo di qui");
                document.getElementById("cur_incident").innerHTML = ""; 
                document.getElementById("id_print_incident").value = "";             
                document.getElementById('name_incident').value  = "";

            	
            	
			}
        }
        xmlhttp.open("GET","delete_action_from_repository_unit.php?action="+action,false);
        xmlhttp.send();		            	
}       
 

// addAction(document.incidents.add_incident.value,id_unit_corrente,document.incidents.add_incident.text)
// document.incidents.id_incident.value,document.incidents.print_incident.value, id_unit_corrente, id_timeline_corrente
function createAction (action, print, unit) {
	
		//window.alert("create_action.php?action="+action+"&"+"print="+print+"&"+"unit="+unit);
	
		if ((action != "") && (unit !="select a unit to edit")){
	
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
            	
            	//commentato per uniformità di comportamento con le altre interfacce
            	//addAction(response,unit,print);

            	
            	removeOptions(document.getElementById("curr_incident"));
				get_actions(unit);
            	
			}
        }
        xmlhttp.open("GET","create_action.php?action="+action+"&"+"print="+print+"&"+"unit="+unit,false);
        xmlhttp.send();	    
	}   else {window.alert ("devi inserire nome dell'azione e/o selezionare una unit");}	            	
}      


  
