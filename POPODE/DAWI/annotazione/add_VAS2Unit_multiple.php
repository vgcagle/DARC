<?php

// This script replaces a set of VAS/VAB in the preconditions/effect of a unit, given the unit and its timeline
// add_VAS2unit_multiple.php?flag=pre&value=2&polarity=at_stake&unit=2&timeline=2

//optionals: at_stake, balanced (only one)


include 'include/connessione.php';

$id_unit = $_REQUEST["unit"];
$id_timeline_unit = $_REQUEST["timeline"];
$flag = $_REQUEST["flag"];


if ($flag == "eff") {$flag = "effectsTimeline";} else {$flag = "preconditionsTimeline";} 

if (isset($_REQUEST["balanced"])) {
	$column = "idValueBalanced";
	$value = $_REQUEST["balanced"];
}

if (isset($_REQUEST["at_stake"])) {
	$column = "idValueAtStake";
	$value = $_REQUEST["at_stake"];
}





$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

//cancello tutti gli stati di tipo VAS (con le feature date: pre o eff, stake o bal) riferiti alla unit e alle timeline data.

/*
DELETE T1, T2
FROM T1
INNER JOIN T2 ON T1.key = T2.key
WHERE condition
*/


$sql = "DELETE State, CSSTimeline FROM ((State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) JOIN Timeline ON $flag = CSSTimeline.Set)"
	. "WHERE typeState = 'VAS' AND  idTimeline = '$id_timeline_unit'  "; 

$conn->query($sql);

echo $sql;

/*
esempio 

DELETE State, CSSTimeline FROM ((State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) JOIN Timeline 
	ON preconditionsTimeline = CSSTimeline.Set)
	WHERE typeState = 'VAS' AND  idTimeline = 10061 
*/	


//poi per ogni value creo un nuovo stato 
//INSERT INTO `State`(`idState`, `typeState`, `printState`, `statusState`, `nameState`, `idValueAtStake`, `idValueBalanced`)
//  VALUES ([value-1],[value-2],[value-3],[value-4],[value-5],[value-6],[value-7])


// devo conoscere Set, lo estraggo da Timeline
$sql = "SELECT `$flag` FROM Timeline WHERE idTimeline = $id_timeline_unit";

$result = $conn->query($sql);
$riga = $result->fetch_assoc();
$id_CSS = $riga[$flag];

$num_value = count($value);

for ($i = 0; $i < $num_value; $i++) {
		$v = $value[$i];
		if ($v == "no") {$v = 0;}
		$sql = "INSERT INTO `State` (`typeState`, `statusState`, `$column`) VALUES ('VAS', 0, $v) ";	

		echo $sql;

		$conn->query($sql);
		$id_state = $conn->insert_id;
		//ora prendo l'id e lo metto nella tabella CCSTimeline
		$sql = "INSERT INTO CSSTimeline (`Set`, printSet, idState) VALUES ($id_CSS, '', $id_state)";	
		$conn->query($sql);

		echo $sql;	
}







?>