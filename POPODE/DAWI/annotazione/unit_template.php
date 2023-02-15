<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Author" content="Vincenzo Lombardo">
<meta name="Description" content="www.cirma.unito.it/labidsi">
<meta name="KeyWords" content="Vincenzo Lombardo, Rossana Damiano, Antonio Pizzo, Annotation of drama">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js" ></script>

<script src="javascriptesterno.js" type="text/javascript"></script>

<script src="javascript_unit.js" type="text/javascript"></script>

<title>Drammar: Unit</title>

<link href="plan_template.css" rel="stylesheet" type="text/css" media="screen" />




<!--
<script src="jquery-1.12.2.min.js"></script>
<script src="script_new.js"></script>
-->



</head>


<?php


echo "<body onload=\"get_units_select (); \" />";
//document.getElementById('id_timeline_corrente').value

?>






<!-- qui metto il valore della unit scelta dall'utente -->
<form name="servizio">
<input type="hidden" id="id_unit_corrente" />
<input type="hidden" id="id_timeline_corrente" />
<input type="hidden" id="check" value="no" />
</form>

<script>
var id_unit_corrente = document.getElementById('id_unit_corrente').value; var id_timeline_corrente = document.getElementById('id_timeline_corrente').value;
</script>

<!-- div id="test">TEST</div -->



<div id="titolo"><center> <a href="absplan_template.php">Abstract Plans</a> | <a href="deplan_template.php">Directly Executable Plans</a> | <a href="index.html">Drammar annotation:</a> UNIT </center></div>



<!-- ############ ############ ############ --> 
<!-- ############ ############ ############ --> 
<!-- ############ UNIT HEADER  ############ -->  
<!-- ############ ############ ############ --> 
<!-- ############ ############ ############ --> 
<div id="unit_header">
  <div id="plan_header_title">Unit Header</div>
    <form name="unit_header_form" id="unit_header_form"> 

    <!-- ############ PREVIOUS UNIT ############ --> 
    <div id="previous">
      <center>Previous unit</center><br/>
      <table style="text-align:left"><col width="20%"><col width="80%">
        <tr><td>name</td><td><span id="campo_unit_precedente">___</span></td></tr>
        <tr><td>description</td><td><span id="description_unit_precedente">___</span></td></tr>
      </table> 
      <input type="hidden" id="id_unit_precedente" />
      <br/>     
      <select id="candidate_previous" <?php  echo "onChange=\"var id_unit_corrente = document.getElementById('id_unit_corrente').value; var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; changePair(this.value,id_unit_corrente,id_timeline_corrente,'pre'); this.selectedIndex = '0'; \""; ?>  
        />
        <option>select previous unit</option>
      </select>
    </div> <!-- div id="previous" -->

    <!--  ############ PREVIOUS ARROW ############# -->    
    <div id="previous_arrow">
      <br/>
      <br/>
      <img src="immagini/arrow.png" width="75%">
    </div>
    
    <!-- ############ UNIT ############ --> 
      <div id="select_or_create_unit">
        <center>Unit</center><br/>
        <select id="id_select_unit" name="select_unit" <?php echo "onChange=\"document.getElementById('check').value = 'ok'; clear_pagina(); setCurrentUnit(this.value); var id_unit_corrente = document.getElementById('id_unit_corrente').value; var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; get_all(this.value,id_timeline_corrente); \"" ; ?>> </select>
        <table style="text-align:left"><col width="30%"><col width="70%">
        <tr class="Testo"><td>name</td><td><span id="id_name_unit_corrente">___</span> </td></tr>
        <tr class="Testo"><td>description</td><td><span id="description_unit_corrente">___</span></td></tr>
        </table> 
        <center><input type="button" id="id_button_delete_unit" value="DELETE UNIT" 
        <?php echo "onClick=\"var id_unit_corrente = document.getElementById('id_unit_corrente').value; var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; deleteUnit(id_unit_corrente, id_timeline_corrente);  clear_pagina(); get_units_select (); resetPagina(); \"" ?>       
        /></center>
        <br />
        <p class="TitoloColonnaNew">new unit</p>
        <table style="text-align:left"><col width="20%"><col width="70%"><col width="10%">
          <tr class="TestoNew"><td>name</td><td><input type="text" id="id_create_unit" name="create_unit" value="type name" /></td></tr>
          <tr class="TestoNew"><td>description</td><td><input type="text" name="print_nuova_unit" id="print_create_unit" value="type description" /></td></tr>
        </table>
        <center>
          <input type="button" value="CLEAR" onclick="document.getElementById('id_create_unit').value = 'type name'; document.getElementById('print_create_unit').value = 'type description';" />
          <!--<input type="button" id="id_button_create_unit" value="CREATE UNIT" <?php echo "" ?>/>-->
          
          <input type="button" id="id_button_create_unit" value="CREATE UNIT" <?php echo "onClick=\"createUnit(document.unit_header_form.create_unit.value, document.unit_header_form.print_nuova_unit.value); document.getElementById('check').value = 'ok'; var id_unit_corrente = document.getElementById('id_unit_corrente').value; setCurrentUnit(id_unit_corrente); var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; clear_pagina(); get_all(id_unit_corrente,id_timeline_corrente); removeOptions(document.getElementById('id_select_unit')); get_units_select (); setRightState (id_unit_corrente);  document.getElementById('description_unit_corrente').innerHTML = document.unit_header_form.print_nuova_unit.value ; \"" ?>
        // setCurrentUnit(id_unit_corrente);  ; 
        />
          
        </center>     
      </div> <!-- div id="select_or_create_unit" -->


<!--  ############ NEXT ARROW ########## -->
    
    <div id="next_arrow">
      <br/>
      <br/>
      <img src="immagini/arrow.png" width="75%">
    </div>

<!--  ############ NEXT UNIT ########## -->

    <div id="next">
      <center>Next unit</center><br/>
      <table style="text-align:left"><col width="20%"><col width="80%">
        <tr><td>name</td><td><span id="campo_unit_seguente" />___</span></td></tr>
        <tr><td>description</td><td><span id="description_unit_seguente">___</span></td></tr>
      </table> 
      <br/>     
      <input type="hidden" id="id_unit_seguente"  />  
      <!-- list of the candidate next units -->
      <select id="candidate_next" <?php  echo "onChange=\"var id_unit_corrente = document.getElementById('id_unit_corrente').value; var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; changePair(this.value,id_unit_corrente,id_timeline_corrente,'eff'); this.selectedIndex = '0'; \""; ?>  
        >
        <option>select next unit</option>
      </select>
    </div> <!-- div id="next" -->
      
    </form>
  </div> <!-- div id="unit_header" -->
      
<!-- ############ ############ ############ --> 
<!-- ############ ############ ############ --> 
<!-- ############ UNIT CONTENT ############ -->  
<!-- ############ ############ ############ --> 
<!-- ############ ############ ############ --> 
  <div id="unit_content">
    <div id="plan_content_title">Unit Content</div>
    
<!-- ############ ############ ############ --> 
<!--  ############ PRECONDITIONS ########## -->
<!-- ############ ############ ############ --> 
    <div id="preconditions">
      <form name="deplan_preconditions_form" id="deplan_preconditions_form">
        <p class="TitoloColonna"><center> Preconditions</center></p>
        <br/>
        Repository
        <select name="select_candidate_pre" id="precondizioni_candidate" onChange="var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; getInfoSolitaryState(this.value, id_timeline_corrente, 'pre'); document.getElementById('precondizioni_unit').selectedIndex = '0'; "><option>candidate preconditions</option> </select>
        <input type="button" value="+" 
        onclick="var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; var state = document.getElementById('precondizioni_candidate').value; addState(state, id_timeline_corrente,'pre');" />         

        <br/>
        Precondition states
		<select name="select_precondition" id="precondizioni_unit" onChange="var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; getInfoState(this.value, id_timeline_corrente, 'pre'); document.getElementById('precondizioni_candidate').selectedIndex = '0';"><option>current preconditions</option></select>
		<input type="button" value="-"
        onclick="var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; var state = document.getElementById('precondizioni_unit').value; deleteState(state, id_timeline_corrente,'pre');" />
        <br/><br/>
<!--  ############ PRECONDITION STATE ########## -->
        <!--Precondition state -->
        <p class="SottoTitoloColonna">State name: <span id="state_name_pre">___</span></p>
      </form>
      <form id="id_p_viewer" name="p_viewer">
        <div>
        <table style="text-align:left"><col width="30%"><col width="70%">
          <!-- tr class="Testo"><td>state</td><td><span id="id_state_p_pre">___</span></td></tr -->
          <tr class="Testo"><td>name</td><td><input name="id_state_pre" type="text" id="id_state_p_no_write_pre" /></td></tr>
          <tr class="Testo"><td>description</td><td><input type="text" name="id_print_state_p_pre"  id="print_state_p_pre" size="75" /></td></tr>
          <tr class="Testo"><td>set</td><td><span id="set_state_p_pre">___</span></td><td></td></tr>
          <tr class="Testo"><td>type</td><td><select  name="id_type_state_p_pre" id="type_state_p_pre"><option value="NIL">select a type</option><option value="BEL">BEL</option><option value="VAS">VAS</option><option value="SOA">SOA</option><option value="ACC">ACC</option></select></td><td></td></tr>
          <tr class="Testo"><td>status</td><td><select name="id_status_state_p_pre" id="status_state_p_pre" ><option value="4">select a status</option><option value="1">true</option><option value="0">false</option><option value="2">none</option></select></td><td></td></tr>
          <tr class="Testo"><td>rec level</td><td><span id="rec_level_state_p_pre"></span></td><td></td></tr>
        </table> 
        <center>
          <input type="button" id="id_delete_state_repository_pre" value="DELETE FROM REPOSITORY"  />
          
          
          <input type="button" value="CLEAR" onclick="document.getElementById('id_p_viewer').reset(); " />
          <input type="button" value="CREATE NEW" onclick="var plan = document.getElementById('id_select_unit').value; var my_print = document.getElementById('print_state_p_pre').value; createState(document.p_viewer.id_state_pre.value,document.p_viewer.id_type_state_p_pre.value, document.p_viewer.id_status_state_p_pre.value, my_print, plan, 'pre');" />
        </center>
        </div>
      </form>
    </div> <!-- preconditions -->

<!-- ############ ############ ############ --> 
<!--  ############ INCIDENTS ########## -->
<!-- ############ ############ ############ --> 
      <div id="incidents">
        <center>Incident</center>
        <br/>
        <form id="form_incidents" name="incidents">
          <select  id="cand_incident" name="add_incident" <?php  echo "onChange=\"getInfoAction(this.value); document.getElementById('curr_incident').selectedIndex = '0';\""; ?> >
		    <option>candidate actions</option>
		</select>
		<input type="button" value="+" id="id_button_addsubplan" 
		<?php  echo "onclick=\"var id_unit_corrente = document.getElementById('id_unit_corrente').value; var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; addAction(document.incidents.add_incident.value,id_unit_corrente,document.incidents.add_incident.text); document.incidents.add_incident.selectedIndex = '0'; \" "; ?> 
		/> 
		
        <select id="curr_incident" name="list_incidents" <?php echo "onChange=\"getInfoAction(this.value); document.getElementById('cand_incident').selectedIndex = '0'; \""; ?>>
          <option>current actions</option></select>
          <!-- deleteAction (plan,action) -->
        <input type="button" value="-" onclick="deleteAction(document.getElementById('id_select_unit').value, document.getElementById('curr_incident').value);" />
        <br/><br/>
        Action: <span id="cur_incident">___</span> 
        <table style="text-align:left"><col width="30%"><col width="70%">
          <!-- tr class="Testo"><td>name</td><td><input  name="id_incident" id="id_id_name" type="text" /></td></tr -->
          
          <tr class="Testo"><td>name</td><td><input type="text" id="name_incident" size="75"/></td></tr>
          <tr class="Testo"><td>description</td><td><input  name="print_incident" id="id_print_incident" type="text" size="75" /></td></tr>
        </table> 
        
        
        <center>        
            <input type="button" value="DELETE FROM REPOSITORY" onclick="deleteActionFromRep(document.getElementById('cand_incident').value);get_actions(document.getElementById('id_select_unit').value);" />
          <input type="hidden" id="id_hidden_action" />
          <input type="button" value="CLEAR" onclick="document.getElementById('form_incidents').reset(); document.getElementById('cur_incident').innerHTML = '____'" />
          <input type="button" value="CREATE NEW ACTION" onclick="createAction(document.getElementById('name_incident').value, document.getElementById('id_print_incident').value, document.getElementById('id_select_unit').value);" />
		</center>
        </form> <!-- id="form_incidents" name="incidents" -->
      </div> <!-- incidents -->
           
<!-- ############ ############ ############ --> 
<!--  ############ EFFECTS ########## -->
<!-- ############ ############ ############ --> 
    <div id="effects">
      <form name="deplan_effects_form" id="deplan_effects_form">
        <center> Effects</center>
        <br/>
        Repository
        <select name="select_candidate_eff" id="effetti_candidati" onChange="var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; getInfoSolitaryState(this.value, id_timeline_corrente,'eff'); document.getElementById('effetti_unit').selectedIndex = '0';"><option>candidate effects</option></select><input type="button" value="+"
      onclick="var id_timeline_corrente = document.getElementById('id_timeline_corrente').value;; var state = document.getElementById('effetti_candidati').value; addState(state,id_timeline_corrente,'eff');"  />        
        <br/>
        Effect states
		<select name="select_effect" id="effetti_unit" onChange="var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; getInfoState(this.value, id_timeline_corrente, 'eff'); document.getElementById('effetti_candidati').selectedIndex = '0';"><option>current effects</option></select>
       <input type="button" value="-" onclick="var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; var state = document.getElementById('effetti_unit').value; deleteState(state,id_timeline_corrente,'eff');" />
        <br/><br/>
<!--  ############ EFFECT STATE ########## -->
        <!--Effect state -->
        State name: <span id="state_name_eff">___</span><br/>
      </form>        
      <form id="id_e_viewer" name="e_viewer">
        <div>
        <table style="text-align:left"><col width="30%"><col width="70%">
          <!-- tr class="Testo"><td>state</td><td><span id="id_state_p_eff"></span></td></tr -->
          <tr class="Testo"><td>name</td><td><input name="id_state_eff" type="text" id="id_state_p_no_write_eff" /></td></tr>
          <tr class="Testo"><td>description</td><td><input type="text" id="print_state_p_eff" size="75"/></td></tr>
          <tr class="Testo"><td>set</td><td><span id="set_state_p_eff">_____</span></td></tr>
          <tr class="Testo"><td>type</td><td><select  name="id_type_state_p_eff" id="type_state_p_eff"><option value="NIL">select a type</option><option value="BEL">BEL</option><option value="VAS">VAS</option><option value="SOA">SOA</option><option value="ACC">ACC</option></select></td></tr>
          <tr class="Testo"><td>status</td><td><select name="id_status_state_p_eff" id="status_state_p_eff" /><option value="4">select a status</option><option value="1">true</option><option value="0">false</option><option value="2">none</option></select></td></tr>
          <tr class="Testo"><td>rec level</td><td><span id="rec_level_state_p_eff"></span></td></tr>
        </table> 
        <center>
          <input type="button" id="id_delete_state_repository_eff" value="DELETE FROM REPOSITORY"  />
          <input type="button" value="CLEAR" onclick="document.getElementById('id_e_viewer').reset(); document.getElementById('state_name_eff').innerHTML = '_____'; " " />
          <input type="button" value="CREATE NEW"  onclick="var plan = document.getElementById('id_select_unit').value; var my_print = document.getElementById('print_state_p_eff').value; var state= document.getElementById('id_state_p_no_write_eff').value; createState(state, document.e_viewer.id_type_state_p_eff.value, document.e_viewer.id_status_state_p_eff.value, my_print, plan, 'eff');"/>
        </center>
        </div>
        </form>
      </div> <!-- effects -->

    </form>
     
    
  </div> <!-- unit_content -->
  

  
<!-- /div -->

</body>
</html>
