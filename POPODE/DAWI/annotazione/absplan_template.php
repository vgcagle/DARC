<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Author" content="Vincenzo Lombardo">
<meta name="Description" content="www.cirma.unito.it/labidsi">
<meta name="KeyWords" content="Vincenzo Lombardo, Rossana Damiano, Antonio Pizzo, Giacomo Albert, Drammar, drama annotation, CIRMA">

<title>Drammar: Abstract plan</title>

<link href="plan_template.css" rel="stylesheet" type="text/css" media="screen" />
<!-- script src="javascriptesterno01.js" type="text/javascript"></script -->

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js" ></script>

<!--<script src="jquery-1.12.2.min.js"></script>-->

<script src="script_new_ABSTRACT_PLANS.js"></script>


</head>

<body>

<!-- #########  -->
<form name="servizio">
<input type="hidden" id="id_piano_nascosto" />
<input type="hidden" id="id_timeline_corrente" />
</form>


<!-- #########  -->
<script>
//var id_unit_corrente = document.getElementById('id_unit_corrente').value; var id_timeline_corrente = document.getElementById('id_timeline_corrente').value;
</script>

<!-- #########  -->
<!-- div id="test">TEST</div -->

<!--<div id="titolo">Drammar annotation: abstract plan</div>-->

<div id="titolo"><center><a href="unit_template.php">Units</a> | <a href="deplan_template.php">Directly Executable Plans</a> | <a href="index.html">Drammar annotation:</a> ABSTRACT PLANS </center></div>


<!-- div id="distance"></div -->
<!-- div id="container" -->
  
  
<!-- ############ ############ ############ --> 
<!-- ############ ############ ############ --> 
<!-- ########## ABSPLAN  HEADER ########### -->  
<!-- ############ ############ ############ --> 
<!-- ############ ############ ############ --> 
  <div id="plan_header">
    <div class="TitoloSezione" id="plan_header_title"><span>Abstract Plan Header</span></div>

    <form id="plan_header_form" id="plan_header_form"> 
      <!-- ############ AGENT ############ --> 
      <div id="agent">
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

      <!-- ############ MAPPING FIRST ############ --> 
      <div id="mapping_first">
        <p><center>mapping first</center></p><br/>
        <p><input type="hidden" id="id_mapping_first"  />  
           <!--<select id="candidate_mapping_first" ><option>Select mapping first</option></select><input type="button" id="id_button_mapping_first" value="+" /></p>-->
        <table style="text-align:left"><col width="20%"><col width="80%">
          <tr class="Testo"><td>name</td><td><span id="campo_mapping_first" />___</span></td></tr>
          <tr class="Testo"><td>description</td><td><span id="description_mapping_first">___</span></td></tr>
        </table>     
      </div> <!-- div id="mapping_first" -->
      
      <!-- ############ PLAN ############ --> 
      <div id="select_or_create_absplan">
        <center>plan</center><br/>
        <select id="id_select_absplan" name="select_absplan"> </select>
        <table style="text-align:left"><col width="30%"><col width="70%">
        <tr class="Testo"><td>name</td><td><span id="id_name_absplan_corrente">___</span> </td></tr>
        <tr class="Testo"><td>description</td><td><span id="description_absplan_corrente">___</span></td></tr>
        </table> 
        <center><input type="button" id="id_button_delete_plan" value="DELETE PLAN" <?php echo "" ?>/></center>
        <br />
        <p class="TitoloColonnaNew">new abstract plan</p>
        <table style="text-align:left"><col width="20%"><col width="70%"><col width="10%">
          <tr class="TestoNew"><td>name</td><td><input type="text" id="id_create_absplan" name="create_absplan" value="type name" /></td></tr>
          <tr class="TestoNew"><td>description</td><td><input type="text" name="print_nuovo_absplan" id="print_create_absplan" value="type description" /></td></tr>
          <tr class="TestoNew"><td>accomplished</td><td>yes<input name="accomplished" type="radio" value="1" id="id_accomplished" checked/ > no<input name="accomplished" type="radio" value="1" id="id_accomplished"/ ></td></tr>
        </table>
        <center>
          <input type="button" value="CLEAR" onclick="document.getElementById('id_create_absplan').value = 'type name'; document.getElementById('print_create_absplan').value = 'type description';" />
          <input type="button" id="id_button_create_absplan" value="CREATE ABSPLAN" <?php echo "" ?>/>
        </center>     
      </div> <!-- div id="select_or_create_absplan" -->
      
      <!-- ############ MAPPING LAST ############ --> 
      <div id="mapping_last">
        <center>mapping last</center><br/>
        <input type="hidden" id="id_mapping_last"  />
        <!--<select id="candidate_mapping_last"><option>select mapping last</option></select><input type="button" id="id_button_mapping_last" value="+" />-->
        <table style="text-align:left"><col width="20%"><col width="80%">
          <tr class="Testo"><td>name</td><td><span id="campo_mapping_last" />___</span></td></tr>
          <tr class="Testo"><td>description</td><td><span id="description_mapping_last">___</span></td></tr>
        </table> 
      </div> <!-- div id="mapping_last" -->

      <!-- ############ GOAL ############ --> 
      <div id="goal">
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
      
    </form>

  </div> <!-- id="plan_header" -->

  <br/>
   
      
<!-- ############ ############ ############ --> 
<!-- ############ ############ ############ --> 
<!-- ########### ABSPLAN CONTENT ########### -->  
<!-- ############ ############ ############ --> 
<!-- ############ ############ ############ --> 
  <div id="plan_content">
    <div class="TitoloSezione" id="plan_content_title"><span>Abstract Plan Content</span></div>
    <form id="unit_content_form"> 
<!-- ############ ############ ############ --> 
<!--  ############ PRECONDITIONS ########## -->
<!-- ############ ############ ############ --> 
    <div id="preconditions">
      <form name="deplan_preconditions_form" id="deplan_preconditions_form">
        <p class="TitoloColonna"><center> Preconditions</center></p>
        <br/>
        Repository
        <select name="select_candidate_pre" id="precondizioni_candidate"><option>candidate preconditions</option> </select>
        <input type="button" value="+" 
        onclick="var plan = document.getElementById('id_select_absplan').value; var state = document.getElementById('precondizioni_candidate').value; addState(state, plan,'pre');" />         

        <br/>
        Precondition states
		<select name="select_precondition" id="precondizioni_unit" onChange="getInfoState(this.value,'pre');"><option>current preconditions</option></select>
		<input type="button" value="-"
        onclick="var plan = document.getElementById('id_select_absplan').value; var state = document.getElementById('precondizioni_unit').value; deleteState(state, plan,'pre');" />
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
          <input type="button" value="DELETE FROM REPOSITORY" onclick="var state =  document.getElementById('precondizioni_candidate').value; var plan = document.getElementById('id_select_absplan').value;  deleteStateFromRep(state, plan);" />
          <input type="button" value="CLEAR" onclick="document.getElementById('id_p_viewer').reset(); document.getElementById('state_name_pre').innerHTML = '_____'; document.getElementById('set_state_p_pre').innerHTML = '_____' ;" />
          <input type="button" value="CREATE NEW" onclick="var plan = document.getElementById('id_select_absplan').value; var my_print = document.getElementById('print_state_p_pre').value; createState(document.p_viewer.id_state_pre.value,document.p_viewer.id_type_state_p_pre.value, document.p_viewer.id_status_state_p_pre.value, my_print, plan, 'pre');" />
        </center>
        </div>
      </form>
    </div> <!-- preconditions -->
<!-- ############ ############ ############ --> 
<!--  ############ SUBPLANS ########## -->
<!-- ############ ############ ############ --> 
      <div id="incidents">
        <center>Subplans</center>
        <br/>
        <form id="form_subplans" name="subplans">
          <select  id="id_cand_subplan" name="cand_subplan" <?php  //echo "onChange=\"getInfoActions(this.value);\""; ?> >
		    <option>candidate subplans</option>
		</select>
		<span align:"right"><select id="id_dopo" name="dopo"><option>insert after ...</option></select></span>
		<input type="button" value="+" id="id_button_addsubplan" <?php  // echo "onclick=\"var id_unit_corrente = document.getElementById('id_unit_corrente').value; var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; addAction(document.incidents.add_incident.value,id_unit_corrente,document.incidents.add_incident.text);\""; ?>  
		/> 
		<br/>
        <select id="id_curr_subplans" name="curr_subplans">
          <option>current subplans</option></select>
        <input type="button" value="-" id="id_button_detachsubplan"<?php  ?> />
		<br/>
        <br/>
        Subplan: <span id="cur_subplan">___</span> 
        <table style="text-align:left"><col width="30%"><col width="70%">
          <!-- tr class="Testo"><td>name</td><td><input  name="id_incident" id="id_id_name" type="text" /></td></tr -->
          <tr class="Testo"><td>description</td><td><input  name="print_incident" id="id_print_incident" type="text" size="75" /></td></tr>
          <tr class="Testo"><td>agent</td><td><span id="id_agent">___</span></td></tr>
          <tr class="Testo"><td>goal</td><td><span id="id_goal">___</span></td></tr>
          <tr class="Testo"><td>accomplished</td><td><span id="id_flag_accomplished">___</span></td></tr>
          <tr class="Testo"><td>mapping_init</td><td><span id="id_mapping_init">___</span></td></tr>
          <tr class="Testo"><td>mapping_end</td><td><span id="id_mapping_end">___</span></td></tr>
        </table> 
        <center><input type="button" value="CLEAR" onclick="document.getElementById('form_subplans').reset();" /></center>

        Current subplans: <span id="id_display_subplans">___</span> 

        </form> <!-- id="form_subplans" name="subplans" -->
      </div> <!-- incidents -->
<!-- ############ ############ ############ --> 
<!--  ############ EFFECTS ########## -->
<!-- ############ ############ ############ --> 
    <div id="effects">
      <form name="deplan_effects_form" id="deplan_effects_form">
        <center> Effects</center>
        <br/>
        Repository
        <select name="select_candidate_eff" id="effetti_candidati" onChange="getInfoState(this.value,'eff');"><option>candidate effects</option></select><input type="button" value="+"
      onclick="var plan = document.getElementById('id_select_absplan').value; var state = document.getElementById('effetti_candidati').value; addState(state,plan,'eff');"  />        

        <br/>
        Effect states
		<select name="select_effect" id="effetti_unit" onChange="getInfoState(this.value,'eff');"><option>current effects</option></select>
       <input type="button" value="-" onclick="var plan = document.getElementById('id_select_absplan').value; var state = document.getElementById('effetti_unit').value; deleteState(state,plan,'eff');" />
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
          <input type="button" value="DELETE FROM REPOSITORY" onclick="var state = document.getElementById('effetti_candidati').value; var plan = document.getElementById('id_select_absplan').value; deleteStateFromRep(state, plan); " />
          <input type="button" value="CLEAR" onclick="document.getElementById('id_e_viewer').reset(); document.getElementById('state_name_eff').innerHTML = '_____'; " " />
          <input type="button" value="CREATE NEW"  onclick="var plan = document.getElementById('id_select_absplan').value; var my_print = document.getElementById('print_state_p_eff').value; var state= document.getElementById('id_state_p_no_write_eff').value; createState(state, document.e_viewer.id_type_state_p_eff.value, document.e_viewer.id_status_state_p_eff.value, my_print, plan, 'eff');"/>
        </center>
        </div>
        </form>
      </div> <!-- effects -->
  </div> <!-- unit_content -->
  
<!-- /div -->

</body>
</html>
