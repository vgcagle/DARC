//gestione parziale delle Unit

// cancello uno stato dalla repository
$(document).ready(function(){
    $("#id_delete_state_repository_pre").click(function(){
 
 		 		//precondizioni_candidate
 		//precondizioni_unit
 
 		var flag = "pre";
    	var state_to_del = $("#precondizioni_candidate").val();
    	var timeline = $('#id_timeline_corrente').val();
    	var unit = $("#id_select_unit").val();
    	
    	//window.alert("delete_state_from_repository.php?state="+state_to_del);

 		//if di verifica che sia valorizzato lo state_to_del
    	if (state_to_del == "select a state") {window.alert("please select a state from the repository");} else {
    	
    	
        $.get("delete_state_from_repository.php?state="+state_to_del, function(data, status){
			
				//verifico se l'azione da cancellare è in quel momento visualizzata negli effetti
				//se sì, la cancello
				if ($("effetti_candidati").val() == state_to_del || $("effetti_unit").val() == state_to_del) {
					
					document.getElementById("id_state_p_no_write_eff").value = "";
					document.getElementById("state_name_eff").innerHTML = "";  
                	document.getElementById("print_state_p_eff").value = "";
                	document.getElementById("rec_level_state_p_eff").innerHTML = '-0'; 
                	document.getElementById("id_p_viewer").reset();

					
				}
			
			 	
			 	removeOptions(document.getElementById("precondizioni_unit"));
				removeOptions(document.getElementById("precondizioni_candidate"));
				removeOptions(document.getElementById("effetti_candidati"));
				removeOptions(document.getElementById("effetti_unit"));
			 	get_states(unit, timeline);

			 	
			 	document.getElementById("id_state_p_no_write_"+flag).value = "";
				document.getElementById("state_name_"+flag).innerHTML = "";  
                document.getElementById("print_state_p_"+flag).value = "";
                document.getElementById("rec_level_state_p_"+flag).innerHTML = '-0'; 
                document.getElementById("id_e_viewer").reset();
				//document.getElementById("id_e_viewer").reset();
				
				
                    
                         
                
    
    });
    }
    
  });   
});

//id_delete_state_repository_eff"
$(document).ready(function(){
    $("#id_delete_state_repository_eff").click(function(){
 
 		//effetti_candidati
		//effetti_unit
 
 		var flag = "eff";
    	var state_to_del = $("#effetti_candidati").val();
    	var timeline = $('#id_timeline_corrente').val();
    	var unit = $("#id_select_unit").val();
    	
    	//window.alert("delete_state_from_repository.php?state="+state_to_del);

 		//if di verifica che sia valorizzato sia unit che state_to_del
    	if (state_to_del == "select a state") {window.alert("please select a state from the repository");} else {
    	
        $.get("delete_state_from_repository.php?state="+state_to_del, function(data, status){
			
			 	removeOptions(document.getElementById("precondizioni_unit"));
				removeOptions(document.getElementById("precondizioni_candidate"));
			 	removeOptions(document.getElementById("effetti_candidati"));
				removeOptions(document.getElementById("effetti_unit"));
			 	get_states(unit, timeline);

			 	
			 	document.getElementById("id_state_p_no_write_"+flag).value = "";
				document.getElementById("state_name_"+flag).innerHTML = "";  
                document.getElementById("print_state_p_"+flag).value = "";
                document.getElementById("rec_level_state_p_"+flag).innerHTML = '-0'; 
                document.getElementById("id_e_viewer").reset();
				//document.getElementById("id_e_viewer").reset();
                    
                         
                
    
    });
    }
  });   
});
