<?php

include 'include/connessione.php';

$id_state = $_REQUEST["state"];

if (!($connessione = mysql_connect($host, $username, $password)))
	 		die("Connessione fallita!");

if (!(mysql_select_db($db,$connessione)))
	 		die("Data base non trovato!");	

mysql_set_charset('utf8',$connessione);	 		
	 		
//cancellazione dello state 
//lo state deve essere cancellato tutto dove compare!
//CSSPlan
//CSSUnit


$sql = "DELETE FROM CSSPlan WHERE `idState` = $id_state";
echo $sql;
echo "<br/>";


if (!($result = mysql_query($sql, $connessione)))
		 	die("Non riesco a cancellare lo State dalla tabella CSSPlan");
	 	

$sql = "DELETE FROM CSSTimeline WHERE `idState` = $id_state";
echo $sql;
echo "<br/>";

if (!($result = mysql_query($sql, $connessione)))
		 	die("Non riesco a cancellare lo State dalla tabella CSSTimeline");		 	


	 		
$sql = "DELETE FROM `State` WHERE `idState` = $id_state";
echo $sql;



if (!($result = mysql_query($sql, $connessione)))
		 	die("Non riesco a cancellare lo State dalla tabella State");




?>