<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Author" content="Vincenzo Lombardo">
<meta name="Description" content="www.cirma.unito.it/labidsi">
<meta name="KeyWords" content="Vincenzo Lombardo, Rossana Damiano, Antonio Pizzo, Annotation of drama">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js" ></script>

<!--<script src="javascriptesterno.js" type="text/javascript"></script>-->

<!--<script src="javascript_unit.js" type="text/javascript"></script>-->

<script src="script_ups.js" type="text/javascript"></script>

<title>Drammar: UPS</title>

<link href="plan_template_new.css" rel="stylesheet" type="text/css" media="screen" />
<!-- script src="javascriptesterno01.js" type="text/javascript"></script -->



<!--<script src="jquery-1.12.2.min.js"></script>
<script src="script_new.js"></script>-->



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



<div id="titolo"><center> <a href="../ABSTRACT_PLANS/absplan_template.php">Abstract Plans</a> | <a href="../DE_PLANS/deplan_template.php">Directly Executable Plans</a> | <a href="../index.html">Drammar annotation:</a> UNIT </center></div>



<!-- div id="distance"></div -->

<!-- div id="container" -->

<!-- ############ ############ ############ --> 
<!-- ############ ############ ############ --> 
<!-- ############ UNIT HEADER  ############ -->  
<!-- ############ ############ ############ --> 
<!-- ############ ############ ############ --> 
<div id="ups_header">
  <div id="plan_header_title">Unit, Incident</div>
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

<!-- ############ ############ ############ --> 
<!--  ############ INCIDENTS ########## -->
<!-- ############ ############ ############ --> 
      <div id="incidents_in_unit">
        <center>Incident</center>
        <br/>
        <form id="form_incidents" name="incidents">
          <select  id="cand_incident" name="add_incident" <?php  echo "onChange=\"getInfoAction(this.value); document.getElementById('curr_incident').selectedIndex = '0';\""; ?> >
		    <option>candidate actions</option>
		</select>
		<input type="button" value="+" id="id_button_addsubplan" 
		<?php  echo "onclick=\"var id_unit_corrente = document.getElementById('id_unit_corrente').value; var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; var candidate_action = document.getElementById('cand_incident').value; var print_candidate = document.getElementById('cand_incident').text; addAction(candidate_action,id_unit_corrente,print_candidate); document.getElementById('cand_incident').selectedIndex = '0'; \" "; ?> 
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

        </form> <!-- id="form_incidents" name="incidents" -->
      </div> <!-- incidents -->
      
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
  <div id="ups_content">
    <div id="plan_content_title">Conflicts and Scenes (with Plans and Goals)</div>
    
<!-- ############ ############ ############ --> 
<!--  ############ PRECONDITIONS ########## -->
<!-- ############ ############ ############ --> 
    <form id="plan_header_form" id="plan_header_form"> 

      <!-- ############ AGENT ############ --> 
      <div id="ups_agent">
        <center>agent</center><br/>
        <select id="id_select_agent" name="select_agent" ><option>Select an agent</option></select><input type="button" value="+" id="id_button_add_agent" /><br/>
        <table style="text-align:left"><col width="30%"><col width="70%">        
          <tr class="Testo"><td>name</td><td><span id="id_name_agent_corrente">___</span></tr>
          <tr class="Testo"><td>description</td><td><span id="id_description_agent_corrente">___</span></td></tr>
        </table> 
        <center><input type="button" value="DELETE AGENT" id="id_button_delete_agent" /></center>
        <br />
        <p class="TitoloColonnaNew">new agent</p>  
        <table style="text-align:left"><col width="30%"><col width="70%">
        <tr class="TestoNew"><td>name</td><td><input type="text" id="id_create_agent" name="create_agent" value="type name " /></td></tr>
        <tr class="TestoNew"><td>description</td><td><input type="text" name="print_nuovo_agent" id="id_print_create_agent" value="type description " /></td></tr>
        </table>         
        <center>
          <input type="button" value="CLEAR" onclick="document.getElementById('id_create_agent').value = 'type name'; document.getElementById('id_print_create_agent').value = 'type description';"  />
          <input type="button" value="CREATE AGENT" id="id_button_new_agent" />
        <center>
      </div> <!-- div id="agent" -->

      <!-- ############ GOAL ############ --> 
      <div id="ups_goal">
        <center>goal</center><br/>

        <select id="id_select_goal" name="select_goal"><option>Select a goal</option></select></td><td><input type="button" value="+" id="id_goal_add" />
        <table style="text-align:left"><col width="20%"><col width="70%"><col width="10%">
        <tr class="Testo"><td>name</td><td><span id="id_name_goal">___</span></td></tr>
        <tr class="Testo"><td>description</td><td><span id="id_description_goal_corrente">___</span></td></tr>
        </table> 
        <center><input type="button" value="DELETE GOAL"  id="id_goal_delete" /></center>
        <br />
        <p class="TitoloColonnaNew">new goal</p>   
        <table style="text-align:left"><col width="30%"><col width="70%">
        <tr class="TestoNew"><td>name</td><td><input type="text" id="id_create_goal" name="create_goal" value="type name" /></td></tr>
        <tr class="TestoNew"><td>description</td><td><input type="text" name="print_nuovo_goal" id="id_print_create_goal" value="type description" /></td></tr>
        </table>
        <center>
          <input type="button" value="CLEAR" onclick="document.getElementById('id_create_goal').value = 'insert new goal name'; document.getElementById('id_print_create_goal').value = 'descrizione nuovo goal';" />
          <input type="button" id="id_button_new_goal" value="CREATE GOAL" />  
        </center>
      </div> <!-- div id="goal" -->

      <!-- ############ PLAN ############ --> 
      <div id="ups_select_or_create_deplan">
        <center>plan</center><br/>
        <select id="id_select_plan" name="select_plan"></select>
        <table style="text-align:left"><col width="30%"><col width="70%">
        <tr class="Testo"><td>name</td><td><span id="id_name_deplan_corrente">___</span> </td></tr>
        <tr class="Testo"><td>description</td><td><span id="description_deplan_corrente">___</span></td></tr>
        </table> 
        <center><input type="button" id="id_button_delete_plan" value="DELETE PLAN" <?php echo "" ?>/></center>
        <br />
        <p class="TitoloColonnaNew">new directly executable plan</p>
        <table style="text-align:left"><col width="20%"><col width="70%"><col width="10%">
          <tr class="TestoNew"><td>name</td><td><input type="text" id="id_create_deplan" name="create_deplan" value="type name" /></td></tr>
          <tr class="TestoNew"><td>description</td><td><input type="text" name="print_nuovo_deplan" id="print_create_deplan" value="type description" /></td></tr>
          <tr class="TestoNew"><td>accomplished</td><td>yes<input name="accomplished" type="radio" value="1" id="id_accomplished" checked/ > no<input name="accomplished" type="radio" value="1" id="id_accomplished"/ ></td></tr>
        </table>
        <center>
          <input type="button" value="CLEAR" onclick="document.getElementById('id_create_deplan').value = 'type name'; document.getElementById('print_create_deplan').value = 'type description';" />
          <input type="button" id="id_button_create_deplan" value="CREATE DEPLAN" <?php echo "" ?>/>
        </center>     
      </div> <!-- div id="select_or_create_deplan" -->

      <!-- ############ ACCOMPLISHED ############ --> 
      <div id="ups_accomplished_conflict_support">
        <center>Plan behavior</center><br/>
        <p>accomplished?</td><td>yes<input name="accomplished" type="radio" value="1" id="id_accomplished" checked/ > no<input name="accomplished" type="radio" value="1" id="id_accomplished"/ ></p>
        <br>
        <p>in conflict with <select id="id_select_plan" name="select_plan"></select></p>
        <br>
        <p>in support of  <select id="id_select_plan" name="select_plan"></select></p>
        <br>
        <p>value put at stake  <select id="id_select_value" name="select_value"></select></p>
        <br>
        <p>value balanced <select id="id_select_value" name="select_value"></select></p>
      </div> <!-- div id="ups_accomplished_conflict_support" -->

      <div id="ups_more">
      <center><input type="button" value="MORE" onclick="da fare" /></center>
      </div> <!-- div id="ups_more" -->

      
    </form>

  </div> <!-- ups_content -->
  

  
<!-- /div -->

</body>
</html>
