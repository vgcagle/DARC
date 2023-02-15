

<input type="button" value="DELETE" onclick="deleteActionFromRep(document.getElementById('cand_incident').value);get_actions(document.getElementById('id_select_plan').value);" />




var id_unit_corrente = document.getElementById('id_unit_corrente').value; 

var id_timeline_corrente = document.getElementById('id_timeline_corrente').value; 

changePair(this.value,id_unit_corrente,id_timeline_corrente,'pre'); this.selectedIndex = '0';


//associa changePair al pulsante + di previous e next unit