<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Author" content="Vincenzo Lombardo">
<meta name="Description" content="www.cirma.unito.it/labidsi">
<meta name="KeyWords" content="Vincenzo Lombardo, Rossana Damiano, Antonio Pizzo, Annotation of drama">

<!--<script src="jquery-3.1.1.min.js" ></script>-->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js" ></script>

<!--<script src="javascriptesterno.js" type="text/javascript"></script>-->
<!--<script src="javascript_unit.js" type="text/javascript"></script>-->
<script src="script_ups.js" type="text/javascript"></script>


<title>Drammar: UPS</title>

<link href="ups_template.css" rel="stylesheet" type="text/css" media="screen" />
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
<input type="hidden" id="id_plan_corrente" />
</form>



<script>
var id_unit_corrente = document.getElementById('id_unit_corrente').value; 
var id_timeline_corrente = document.getElementById('id_timeline_corrente').value;
</script>

<div id="titolo">
<center> 
<a href="index.html">Drammar annotation </a>: ONE SCREEN ANNOTATION 
</center>
<p class="Titolo">Annotation of drama metadata</p>
</div>


<!-- ############ ############ ############ --> 
<!-- ############ ############ ############ --> 
<!-- ############     UNIT     ############ -->  
<!-- ############ ############ ############ --> 
<!-- ############ ############ ############ --> 

<div id="header_title">Unit</div>
<form name="unit_header_form" id="unit_header_form"> 

<table style="text-align:left"> <col width="150px"><col width="30px"><col width="350px"><col width="30px"><col width="150px">
  <tr class="Testo">

   <!-- ############ PREVIOUS UNIT ############ -->
   <!-- ############ PREVIOUS UNIT ############ -->
   <!-- ############ PREVIOUS UNIT ############ -->
   <td style="vertical-align:top; width:150px; max-width:150px; min-width:150px;"> <!--"  max-width:150px; min-width:150px;" -->
   <div id="previous">
    <center class="TitoloColonna">Previous unit</center>

    <br/>
    <table style="text-align:left;"><!-- col width="50px"><col width="100px" -->
     <tr class="Testo"><td style="border:0px;">name</td><td><span id="campo_unit_precedente">___</span></td></tr>
     <tr class="Testo"><td style="border:0px;">description</td><td><span id="description_unit_precedente">___</span></td></tr>
    </table> 

    <br/> 
    <input type="hidden" id="id_unit_precedente" />
    <div class="styled-select">
     <select id="candidate_previous"><option>select previous unit</option></select>
     <input type="button" value="+" id="id_button_previous_unit" />
    </div>

    <br/>
    <p class="Testo">value at stake before
      <div class="styled-select">
        <select id="id_value_atStake_before" name="select_value"></select></div></p>
    <br/>

    <p class="Testo">value in balance before
      <div class="styled-select">
        <select id="id_value_inBalance_before" name="select_value"></select></div></p>

   </div> <!-- div id="previous" -->

   </td>

   <!--  ############ PREVIOUS ARROW ############# -->    
   <!--  ############ PREVIOUS ARROW ############# -->    
   <!--  ############ PREVIOUS ARROW ############# -->    
   <td style="vertical-align:middle; text-align:center;"> <!-- width:15px" -->
   <!--div id="previous_arrow"-->
     <img src="immagini/arrow.png" width="15px" height="30px">
   <!--/div--> 
   </td>

   <!-- ############ UNIT ############ --> 
   <!-- ############ UNIT ############ --> 
   <!-- ############ UNIT ############ --> 
   <td style="vertical-align:top; background-color: #ffff33;"> <!-- width:350px; max-width:350px; min-width:350px;" -->
   <div id="select_or_create_unit">
   <center>
    <div class="styled-select">
     <select id="id_select_unit" name="select_unit"> </select>
     <input type="button" id="id_button_delete_unit" value="DELETE UNIT" 
       <?php echo "onClick=\"var id_unit_corrente = document.getElementById('id_unit_corrente').value; var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; deleteUnit(id_unit_corrente, id_timeline_corrente);  clear_pagina(); get_units_select (); resetPagina(); \"" ?>       
       />
    </div>
   </center>

    <table style="text-align:left"><col width="30px"><col width="135px"><col width="135px"><col width="50px">
     <tr class="Testo">
      <td>name</td> <!-- style="width:100px; min-width:100px; height:10px; max-height:10px; min-height:10px;">name</td -->
      <td id="id_name_unit_corrente"></td> <!-- style="width:150px; min-width:150px; height:10px; max-height:10px; min-height:10px; ">___</td-->
      <td style="background-color:yellow;"><input id="tit_new" name="titolo" type="text" value=""/></td> <!-- width:150px; min-width:150px; height:10px; max-height:10px; min-height:10px; " -->
      <td style="text-align:center; vertical-align:middle;"><input type="button" id="id_button_new_unit_name" value="update"/></td> <!-- style="width:30px; min-width:30px; height:10px; max-height:10px; min-height:10px; -->
     </tr>
     <tr class="Testo">
      <td>description</td>
      <td id="description_unit_corrente">   </td>
      <td><textarea name="descrizione" id="description_new"></textarea></td>
      <td style="text-align:center; vertical-align:middle;"><input type="button" id="id_button_new_unit_description" value="update"/></td>
     </tr>
     <!--tr class="Testo">
      <td style="width:100px; min-width:100px; height:10px; max-height:10px; min-height:10px;">description</td>
      <td id="description_unit_corrente" style="width:150px; min-width:150px;  max-height:60px; overflow-y:scroll ">    </td>
      <td style="background-color:yellow"><textarea name="descrizione" id="description_new" style="max-width:150px; max-height:60px" \>  </textarea></td>
      <td style="width:30px; min-width:30px; height:10px; max-height:10px; min-height:10px; "><input type="button" id="id_button_new_unit_description" value="update"/>  </td>
     </tr -->
    </table>
    <br />

    <p class="TitoloColonnaNew">new unit</p>
    <table style="text-align:left"><col width="100px"><col width="250px">
     <tr class="TestoNew"><td>name*</td><td><input type="text" id="id_create_unit" name="create_unit" value="type name" /></td></tr>
     <tr class="TestoNew"><td>description</td><td><input type="text" name="print_nuova_unit" id="print_create_unit" value="type description" /></td></tr>
    </table>

    <center>
    <input type="button" value="CLEAR" onclick="document.getElementById('id_create_unit').value = 'type name'; document.getElementById('print_create_unit').value = 'type description';" />
    <!--<input type="button" id="id_button_create_unit" value="CREATE UNIT" <?php echo "" ?>/>-->
    <input type="button" id="id_button_create_unit" value="CREATE UNIT" <?php echo "onClick=\"createUnit(document.unit_header_form.create_unit.value, document.unit_header_form.print_nuova_unit.value);   \"" ?>
      // setCurrentUnit(id_unit_corrente);  ; 
      />
    </center>     
   </div> <!-- div id="select_or_create_unit" -->
  </td>

   <!--  ############ NEXT ARROW ########## -->
  <td style="vertical-align:middle; text-align:center;"> <!-- " width:15px" -->
   <div id="next_arrow">
    <br/>
    <img src="immagini/arrow.png" width="15px" height="30px">
   </div>
  </td>

   <!--  ############ NEXT UNIT ########## -->
  <td style="vertical-align:top; width:150px; max-width:150px; min-width:150px;"> <!-- " width=150px; " -->
   <div id="next">
    <center class="TitoloColonna">Next unit</center><br/>
    <table style="text-align:left"><!-- col width="50px"><col width="100px" -->
     <tr class="Testo"><td style="border:0px;">name</td><td><span id="campo_unit_seguente" />___</span></td></tr>
     <tr class="Testo"><td style="border:0px;">description</td><td><span id="description_unit_seguente">___</span></td></tr>
    </table> 

    <br/>     
    <input type="hidden" id="id_unit_seguente"  />  
    <!-- list of the candidate next units -->
    <div class="styled-select">
     <select id="candidate_next"><option>select next unit</option></select>
     <input type="button" value="+" id="id_button_next_unit" />
    </div>

    <br/>
    <p class="Testo">value at stake after
      <div class="styled-select">
        <select id="id_value_atStake_after" name="select_value"></select></div></p>
    <br/>
    <p class="Testo">value in balance after
      <div class="styled-select">
        <select id="id_value_inBalance_after" name="select_value"></select></div></p>

   </div> <!-- div id="next" -->
  </td>

 </tr>
</table>

</form>


<!-- ############ ############ ############ ############ ############ ############ --> 
<!-- ############ ############ ############ ############ ############ ############ --> 
<!-- ############ ############ PLAN ############ ############ ############ -->  
<!-- ############ ############ ############ ############ ############ ############ --> 
<!-- ############ ############ ############ ############ ############ ############ --> 
<div id="plan_content_title">Plans</div>
<form id="plan_header_form" id="plan_header_form"> 

<table style="text-align:left; vertical-align:top;"> <col width="160px"><col width="150px"><col width="300px"><col width="100px">
  <tr class="Testo">
  <!-- ############ AGENT ############ --> 
  <!-- ############ AGENT ############ --> 
  <!-- ############ AGENT ############ --> 
  <td style="vertical-align:top; width=160px; max-width:160px; min-width:160px;">
   <div id="ups_agent">
    <center class="TitoloColonna">agent</center><br/>
    <div class="styled-select">
     <select id="id_select_agent" name="select_agent" ><option>Select agent</option></select>
     <input type="button" value="+" id="id_button_add_agent" />
     <input type="button" value="DELETE AGENT" id="id_button_delete_agent" />
    </div>
    
    <div style="overflow-x:auto;">
    <table style="text-align:left"> <col width="20%"><col width="70%"><col width="10%">  
     <!-- ############ NAME ############ --> 
     <!-- tr class="Testo">
      <td> <div style="height:15px; max-height:15px; min-height:15px; ">name</div></td>
      <td id="id_name_agent_corrente" style="width:150px; max-width:150px"><div style="height:15px; max-height:15px; min-height:15px; ">   </div></td>
      <td>   </td>
     </tr -->
     <!-- tr class="Testo">
      <td>   </td>
      <td style="background-color:yellow"><div style="height:15px; max-height:15px; min-height:15px; "><input id="new_agent_name" name="titolo" type="text" value=""/></div></td>
      <td><div style="height:15px; max-height:15px; min-height:15px; "><input id="id_button_new_agent_name" type="button" value="update" /></div></td>
     </tr -->
     <tr class="Testo">
      <td>name</td>
      <td id="id_name_agent_corrente"></td>
      <td></td>
     </tr>
     <tr class="Testo">
      <td></td>
      <td style="background-color:yellow"><input id="new_agent_name" name="titolo" type="text" value=""/></td>
      <td><input id="id_button_new_agent_name" type="button" value="update" /></td>
     </tr>

     <!-- ############ DESCRIPTION ############ --> 
     <!-- tr class="Testo">
      <td><div style="height:15px; max-height:15px; min-height:15px;">description</td>
      <td id="id_description_agent_corrente"><div id="descriptions" style="width:150px; height:30px; max-height:60px; ">   </div></td>
      <td>   </td>
     </tr>
     <tr class="Testo">
      <td></td>
      <td style="background-color:yellow"><textarea name="description" id="new_agent_description" style="max-width:150px; height:30px; max-height:60px" \>___</textarea></td>
      <td><div style="height:15px; max-height:15px; min-height:15px;"><input type="button" value="update" id="id_button_new_agent_description" /></td>
     </tr -->
     <tr class="Testo">
      <td>description</td>
      <td id="id_description_agent_corrente"></td>
      <td></td>
     </tr>
     <tr class="Testo">
      <td></td>
      <td style="background-color:yellow"><textarea name="description" id="new_agent_description" \></textarea></td>
      <td><input type="button" value="update" id="id_button_new_agent_description" /></td>
     </tr>

     <!-- ############ VALUES ############ --> 
     <!-- tr class="Testo">
      <td><div style="height:15px; max-height:15px; min-height:15px;">values</td>
      <td id="id_print_values"><div id="values" style="width:150px; max-height:60px;"> </div></td>
      <td></td>
     </tr>
     <tr class="Testo">
      <td></td>
      <td style="background-color:yellow"><textarea name="values" id="new_agent_values" style="max-width:150px; max-height:60px" \>___</textarea></td>
      <td><div style="height:15px; max-height:15px; min-height:15px; "><input type="button" value="update" id="id_button_new_agent_values"/></td>
     </tr -->
     <tr class="Testo">
      <td>values</td>
      <td id="id_print_values"></td>
      <td></td>
     </tr>
     <tr class="Testo">
      <td></td>
      <td style="background-color:yellow"><textarea name="values" id="new_agent_values"  \></textarea></td>
      <td><input type="button" value="update" id="id_button_new_agent_values"/></td>
     </tr>

     <!-- ############ PLEASANT ############ --> 
     <!-- tr class="Testo">
      <td><div style="height:15px; max-height:15px; min-height:15px;">pleasant</td>
      <td id="id_print_pleasant"><div id="pleasant" style="width:150px; max-height:60px;"> </div></td>
      <td></td>
     </tr>
     <tr class="Testo">
      <td></td>
      <td style="background-color:yellow"><textarea name="values" id="new_agent_pleasant" style="max-width:150px; max-height:60px" \>___</textarea></td>
      <td><div style="height:15px; max-height:15px; min-height:15px; "><input type="button" value="update" id="id_button_new_agent_pleasant"/></td>
     </tr -->
     <tr class="Testo">
      <td>
        <p class="Testo">pleasant? </p>
      </td>
      <!-- td id="id_print_pleasant"></td -->
      <td>yes<input name="pleasant" type="radio" value="1" id="id_pleasant_yes" checked/ > no<input name="pleasant" type="radio" value="0" id="id_pleasant_no"/ ></td>
      <td><div style="height:15px; max-height:15px; min-height:15px; "><input type="button" value="update" id="id_button_new_agent_pleasant"/></td>
     </tr>
     <!-- VERSIONE PRECEDENTE
      <tr class="Testo">
      <td></td>
      <td style="background-color:yellow"><textarea name="values" id="new_agent_pleasant" \></textarea></td>
      <td><input type="button" value="update" id="id_button_new_agent_pleasant"/></td>
     </tr -->

     <!-- ############ LIKE ############ --> 
     <!-- tr class="Testo">
      <td><div style="height:15px; max-height:15px; min-height:15px;">like</td>
      <td id="id_print_like"><div id="like" style="width:150px; max-height:60px;"> </div></td>
      <td></td>
     </tr>
     <tr class="Testo">
      <td></td>
      <td style="background-color:yellow"><textarea name="values" id="new_agent_like" style="max-width:150px; max-height:60px" \>___</textarea></td>
      <td><div style="height:15px; max-height:15px; min-height:15px; "><input type="button" value="update" id="id_button_new_agent_like"/></td>
     </tr -->
     <tr class="Testo">
      <td>like</td>
      <td id="id_print_like"></td>
      <td></td>
     </tr>
     <tr class="Testo">
      <td></td>
      <td style="background-color:yellow"><textarea name="values" id="new_agent_like" \></textarea></td>
      <td><input type="button" value="update" id="id_button_new_agent_like"/></td>
     </tr>
     <tr class="Testo">
      <td>dislike</td>
      <td id="id_print_dislike"></td>
      <td></td>
     </tr>
     <tr class="Testo">
      <td></td>
      <td style="background-color:yellow"><textarea name="values" id="new_agent_dislike" \></textarea></td>
      <td><input type="button" value="update" id="id_button_new_agent_dislike"/></td>
     </tr>

    </table> <!-- AGENT -->
    </div>

    <br />

  <!-- ############ NEW AGENT ############ --> 
    <p class="TitoloColonnaNew">new agent</p>  
    <table style="text-align:left"><!-- col width="30%"><col width="70%" -->
     <tr class="TestoNew"><td>name*</td><td><input type="text" id="id_create_agent" name="create_agent" value="type name" /></td></tr>
     <tr class="TestoNew"><td>description</td><td><input type="text" name="print_nuovo_agent" id="id_print_create_agent" value="type description " /></td></tr>
     <tr class="TestoNew"><td>values (separated by comma)</td><td><input type="text" name="print_comma_separated_values" id="id_values" value="type values separated by comma" /></td></tr>
     <tr class="TestoNew">
      <td>pleasant?</td>
      <td>yes<input name="pleasant_create" type="radio" value="1" id="id_pleasant_create_yes" checked/ > no<input name="pleasant_create" type="radio" value="0" id="id_pleasant_create_no"/ ></td>
     </tr>
     <tr class="TestoNew"><td>like objects (separated by comma)</td><td><input type="text" name="print_comma_separated_like" id="id_like" value="type names separated by comma" /></td></tr>
     <tr class="TestoNew"><td>dislike objects (separated by comma)</td><td><input type="text" name="print_comma_separated_dislike" id="id_dislike" value="type names separated by comma" /></td></tr>
    </table>         

    <center>
     <input type="button" value="CLEAR" onclick="document.getElementById('id_create_agent').value = 'type name'; 
                                                document.getElementById('id_print_create_agent').value = 'type description';
                                                document.getElementById('id_values').value = 'type values separated by comma';"  />
     <input type="button" value="CREATE AGENT" id="id_button_new_agent" />
    </center>

   </div> <!-- div id="ups_agent" -->
  </td>

  <!-- ############ GOAL ############ --> 
  <!-- ############ GOAL ############ --> 
  <!-- ############ GOAL ############ --> 
  <td style="vertical-align:top; width=150px; max-width:150px; min-width:150px;">
   <div id="ups_goal">
    <center class="TitoloColonna">goal</center><br/>
    <div class="styled-select">
     <select id="id_select_goal" name="select_goal"><option>Select goal</option></select>
     <input type="button" value="+" id="id_goal_add" />
     <input type="button" value="DELETE GOAL"  id="id_goal_delete" />
    </div>

    <table style="text-align:left"><!-- col width="20px"><col width="80px"><col width="20px">< col width="20%"><col width="70%"><col width="10%"--> 
     <!-- ############ NAME ############ -->
     <!-- tr class="Testo">
      <td> <div style="height:15px; max-height:15px; min-height:15px; ">name</div></td>
      <td id="id_name_goal" style="width:150px; max-width:150px"><div style="height:15px; max-height:15px; min-height:15px; ">___</div></td>
      <td></td>
     </tr>
     <tr class="Testo">
      <td></td>
      <td style="background-color:yellow"><div style="height:15px; max-height:15px; min-height:15px; "><input id="new_goal_name" name="titolo" type="text" /></div></td>
      <td><div style="height:15px; max-height:15px; min-height:15px; "><input type="button" value="update" id="id_button_new_goal_name"/></div></td>
     </tr -->
     <tr class="Testo">
      <td>name</td>
      <td id="id_name_goal"></td>
      <td></td>
     </tr>
     <tr class="Testo">
      <td></td>
      <td style="background-color:yellow"><input id="new_goal_name" name="titolo" type="text"/></td>
      <td style="text-align:center; vertical-align:middle;"><input type="button" value="update" id="id_button_new_goal_name"/></td>
     </tr>
     <!-- ############ DESCRIPTION ############ -->
     <!-- tr class="Testo">
      <td><div style="height:15px; max-height:15px; min-height:15px;">description</td>
      <td id="id_description_goal_corrente"><div id="descriptions" style="width:150px; max-height:60px; overflow-y:scroll "> </div></td>
      <td></td>
     </tr>
     <tr class="Testo">
      <td></td>
      <td style="background-color:yellow"><textarea name="description" id="new_goal_description" style="max-width:150px; max-height:60px" \>___</textarea></td>
      <td><div style="height:15px; max-height:15px; min-height:15px; "><input type="button" value="update" id="id_button_new_goal_description" /></td>
     </tr -->
     <tr class="Testo">
      <td>description</td>
      <td id="id_description_goal_corrente"></td>
      <td></td>
     </tr>
     <tr class="Testo">
      <td></td>
      <td style="background-color:yellow"><textarea name="description" id="new_goal_description" \></textarea></td>
      <td><input type="button" value="update" id="id_button_new_goal_description" /></td>
     </tr>
    </table>

    <br />

 <!-- ############ NEW GOAL ############ --> 
    <p class="TitoloColonnaNew">new goal</p>   
    <table style="text-align:left"><!-- col width="30%"><col width="70%" -->
     <tr class="TestoNew"><td>name*</td><td><input type="text" id="id_create_goal" name="create_goal" value="type name" /></td></tr>
     <tr class="TestoNew"><td>description</td><td><input type="text" name="print_nuovo_goal" id="id_print_create_goal" value="type description" /></td></tr>
    </table>

    <center>
     <input type="button" value="CLEAR" onclick="document.getElementById('id_create_goal').value = 'insert new goal name'; document.getElementById('id_print_create_goal').value = 'descrizione nuovo goal';" />
     <input type="button" id="id_button_new_goal" value="CREATE GOAL" />  
    </center>

   </div> <!-- div id="goal" -->
  </td>

  <!-- ############ PLAN ############ --> 
  <!-- ############ PLAN ############ --> 
  <!-- ############ PLAN ############ --> 
  <td class="TitoloColonna" style="vertical-align:top; width=300px; max-width:300px; min-width:300px;" >

   <div id="ups_select_or_create_deplan">
    <center>plan</center><br/>
    <div class="styled-select">
     <select id="id_select_plan" name="select_plan"><option>Select plan</option></select>
     <input type="button" id="id_button_delete_plan" value="DELETE PLAN" <?php echo "" ?>/>
    </div>

    <table style="text-align:left"><!-- col width="10%"><col width="40%"><col width="40%"><col width="10%" --> 
     <!-- ############ NAME ############ -->
     <!-- tr class="Testo">
      <td> <div style="height:15px; max-height:15px; min-height:15px; ">name</div></td>
      <td id="id_name_deplan_corrente" style="width:150px; max-width:150px"><div style="height:15px; max-height:15px; min-height:15px; ">___</div></td>
      <td style="background-color:yellow"><div style="height:15px; max-height:15px; min-height:15px; "><input id="new_tit_plan" name="titolo" type="text" value=""/></div></td>
      <td><div style="height:15px; max-height:15px; min-height:15px; "><input type="button" id="id_button_new_plan_name" value="update"/></div></td>
     </tr -->
     <tr class="Testo">
      <td>name</td>
      <td id="id_name_deplan_corrente"></td>
      <td style="background-color:yellow"><input id="new_tit_plan" name="titolo" type="text" value=""/></td>
      <td><input type="button" id="id_button_new_plan_name" value="update"/></td>
     </tr>
     <!-- ############ DESCRIPTION ############ -->
     <!-- tr class="Testo">
      <td><div style="height:15px; max-height:15px; min-height:15px;">description</td>
      <td id="description_deplan_corrente"><div id="descriptions" style="width:150px; max-height:60px; overflow-y:scroll "> </div></td>
      <td style="background-color:yellow"><textarea name="description" id="new_description_plan" style="max-width:150px; max-height:60px" \>___</textarea></td>
      <td><div style="height:15px; max-height:15px; min-height:15px; "><input type="button" value="update" id="id_button_new_plan_description"/></td>
     </tr -->
     <tr class="Testo">
      <td>description</td>
      <td id="description_deplan_corrente">   </td>
      <td style="background-color:yellow"><textarea name="description" id="new_description_plan" \></textarea></td>
      <td><input type="button" value="update" id="id_button_new_plan_description"/></td>
     </tr>
     <!-- ############ ACTION ############ -->
     <!--tr class="Testo">
      <td><div style="height:15px; max-height:15px; min-height:15px;">action</td>
      <td id="id_print_values"><div id="name_action" style="width:150px; max-height:60px; overflow-y:scroll "> </div></td>
      <td style="background-color:yellow"><textarea name="action" id="new_action" style="max-width:150px; max-height:60px" \>___</textarea></td>
      <td><div style="height:15px; max-height:15px; min-height:15px; "><input type="button" value="update" id="id_button_new_action"/></td>
     </tr -->
     <tr class="Testo">
      <td>action</td>
      <td id="name_action">   </td>
      <td style="background-color:yellow"><textarea name="action" id="new_action" \></textarea></td>
      <td><input type="button" value="update" id="id_button_new_action"/></td>
     </tr>

    </table>  

    <br />

    <!-- p class="Testo">Select precondition values at stake
    <div class="styled-select">
     <select id="candidate_precond_valuesAtStake"><option>select precondition values at stake</option></select>
     <input type="button" value="+" id="id_button_precond_valuesAtStake" />
    </div>
    </p>

    <br />

    <p class="Testo">Select precondition values balanced
    <div class="styled-select">
     <select id="candidate_precond_valuesBalanced"><option>select precondition values balanced</option></select>
     <input type="button" value="+" id="id_button_precond_valuesBalanced" />
    </div>
    </p>

    <br />

    <p class="Testo">Select effect values at stake
    <div class="styled-select">
     <select id="candidate_effect_valuesAtStake"><option>select effect values at stake</option></select>
     <input type="button" value="+" id="id_button_effect_valuesAtStake" />
    </div>
    </p>

    <br />

    <p class="Testo">Select effect values at stake
    <div class="styled-select">
     <select id="candidate_effect_valuesBalanced"><option>select effect values balanced</option></select>
     <input type="button" value="+" id="id_button_effect_valuesBalanced" />
    </div>
    </p -->

    <br />

  <!-- ############ NEW PLAN ############ --> 
    <p class="TitoloColonnaNew">new directly executable plan</p>
    <table style="text-align:left"><!-- col width="20%"><col width="70%"><col width="10%"-->
     <tr class="TestoNew"><td>name*</td><td><input type="text" id="id_create_deplan" name="create_deplan" value="type name" /></td></tr>
     <tr class="TestoNew"><td>description</td><td><input type="text" name="print_nuovo_deplan" id="print_create_deplan" value="type description" /></td></tr>
     <tr class="TestoNew"><td>action</td><td><input type="text" name="name_new_action" id="id_name_new_action" value="type description" /></td></tr>
    </table>

    <center>
     <input type="button" value="CLEAR" onclick="document.getElementById('id_create_deplan').value = 'type name'; document.getElementById('print_create_deplan').value = 'type description';" />
     <input type="button" id="id_button_create_deplan" value="CREATE DEPLAN" <?php echo "" ?>/>
    </center>     
   </div> <!-- div id="select_or_create_deplan" -->

  </td>

  <!-- ############ RELATIONS ############ --> 
  <!-- ############ RELATIONS ############ --> 
  <!-- ############ RELATIONS ############ --> 
  <td class="TitoloColonna" style="vertical-align:top; width=100px; max-width:100px; min-width:100px;">
   <div id="ups_accomplished_conflict_support">
    <center>relations</center><br/>
    <p class="Testo">accomplished? yes<input name="accomplished" type="radio" value="1" id="id_accomplished_yes" checked/ > no<input name="accomplished" type="radio" value="0" id="id_accomplished_no"/ ></p>
    <br/>

    <p class="Testo">in conflict with 
      <div class="styled-select">
        <select id="id_conflict_plan" name="select_plan"></select></div></p>
    <br/>

    <p class="Testo">in support of  
      <div class="styled-select">
        <select id="id_support_plan" name="select_plan"></select></div></p>
    <br/>

    <p class="Testo">effect value at stake  
      <div class="styled-select">
        <select id="id_conflict_value_e" name="select_value"></select></div></p>
    <br/>

    <p class="Testo">effect value in balance
      <div class="styled-select">
        <select id="id_support_value_e" name="select_value"></select></div></p>
    <br/>

    <p class="Testo">precond value at stake  
      <div class="styled-select">
        <select id="id_conflict_value_p" name="select_value"></select></div></p>
    <br/>

    <p class="Testo">precond value in balance
      <div class="styled-select">
        <select id="id_support_value_p" name="select_value"></select></div></p>

   </div> <!-- div id="ups_accomplished_conflict_support" -->
  </td>

 </tr>

</table>

</form>

</body>

</html>

