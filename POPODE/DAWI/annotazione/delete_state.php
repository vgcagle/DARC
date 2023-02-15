<?php

include 'include/connessione.php';
//CANCELLA UNO STATO DALLE PRECONDIZIONI O EFFETTI DI UNA UNIT

//delete_state.php?state=7&timeline=4&flag=pre
//

$id_state = $_REQUEST["state"];
$id_timeline_unit = $_REQUEST["timeline"];
$flag = $_REQUEST["flag"];

if ($_REQUEST["flag"] == "pre") {$field = "Timeline.preconditionsTimeline"; }
if ($_REQUEST["flag"] == "eff") {$field = "Timeline.effectsTimeline"; }

if (!($connessione = mysql_connect($host, $username, $password)))
	 		die("Connessione fallita!");

if (!(mysql_select_db($db,$connessione)))
	 		die("Data base non trovato!");	


mysql_set_charset('utf8',$connessione);

//recupero CSS della Unit
$sql = "SELECT CSSTimeline.Set, printSet FROM CSSTimeline JOIN Timeline ON $field = CSSTimeline.Set WHERE idTimeline = $id_timeline_unit" ;    
//echo $sql;

if (!($result = mysql_query($sql, $connessione)))
		 	die("Non riesco a eseguire la query che recupera il CSS della unit");

$riga = mysql_fetch_array($result);

$set = $riga["Set"];
$printSet = $riga["printSet"];


//DELETE FROM `DrammarAnnotation`.`CSSTimeline` WHERE `csstimeline`.`id` = 9
$sql = "DELETE FROM `DrammarAnnotation`.`CSSTimeline` WHERE idState = '$id_state' AND `Set` = $set";
echo $sql;

if (!($result = mysql_query($sql, $connessione)))
		 	die("Non riesco a eseguire la query che cancella lo stato dal CSS");
 	
		 	

?>