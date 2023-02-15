<?php

// This script replaces a VAS/VAB in the preconditions/effect of a unit, given the unit and its timeline
// add_VAS2unit.php?flag=pre&value=2&polarity=at_stake&unit=2&timeline=2

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


if ($value == "no") {$value = 0;}



$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");




// la Unit viene creata con la sua timeline con associati il set delle precondizioni e degli effetti:
// basta cancellare dalla tabella degli stati il singolo stato con il VAS, non serve cancellare il CSS effetti o precondizioni dalla tabella Timeline
// *** dubbio: bisogna cancellare il riferimento allo stato dalla tabella CSS timeline? Ora come ora, resta un stato "vuoto" ***

// Prima provo a aggiornare il valore dello stato con il VAS (mettendolo a 0 se il VAS vale "no")
// per conoscere l'id dello stato da cancellare devo metterlo in join con la tabella dei CSS
$sql = "UPDATE ((State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) JOIN Timeline ON $flag = CSSTimeline.Set)".
	"SET `$column` = 0 WHERE typeState = 'VAS' AND  idTimeline = '$id_timeline_unit'  "; 

//echo $sql;
//echo "<br />";


if ($conn->query($sql) === TRUE) {
    $id_state = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$sql = "UPDATE ((State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) JOIN Timeline ON $flag = CSSTimeline.Set)".
	"SET `$column` = $value  WHERE typeState = 'VAS' AND  idTimeline = '$id_timeline_unit'  "; 

//echo $sql;
//echo "<br />";

// UPDATE ((State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) JOIN Timeline ON PreconditionsTimeline = CSSTimeline.Set) SET idValueAtStake = 0  WHERE typeState = 'VAS' AND  idTimeline = 2  
// UPDATE ((State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) JOIN Timeline ON EffectsTimeline = CSSTimeline.Set) SET idValueAtStake = 0 WHERE typeState = 'VAS' AND idTimeline = 2



if ($conn->query($sql) === TRUE) {
    $id_state = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}


// ATTENZIONE: se il valore è uguale a quello già presente... le modifica non avviene... !
$n_righe = $conn->affected_rows;
//echo "righe modificate $n_righe";

//testo se l'update ha funzionato: se no, faccio una insert
//(per ora, tutti gli altri campi tranne il value e il type sono messi a '' per evitare problemi con i null values sul server Altervista)
//devo inserire anche il CSSState, che è scritto nella tabella Timeline
if ( $n_righe == 0) {

	

	// creo un nuovo stato nella tabella degli stati che punta al value dato
	$sql = "INSERT INTO State (typeState, printState, statusState, nameState, $column) VALUES ('VAS', '', 0, '', $value)";
	//echo $sql;
	//echo "<br/>";

	$conn->query($sql);
	$id_state = $conn->insert_id;


	// ora aggiungo lo stato al CSSTimeline delle precondizioni (o effetti) della unit
	// devo conoscere Set e idState... Set lo estraggo da Timeline
	$sql = "SELECT `$flag` FROM Timeline WHERE idTimeline = $id_timeline_unit";
	//echo $sql;
	//echo "<br/>";

	$result = $conn->query($sql);
	$riga = $result->fetch_assoc();
	$id_CSS = $riga[$flag];

	// collego il nuovo stato al CSS giusto, $id_CSS
	$sql = "INSERT INTO CSSTimeline (`Set`, printSet, idState) VALUES ($id_CSS, '', $id_state)";
	//echo $sql;
	//echo "<br/>";
	
	$conn->query($sql);

//$n_righe = $conn->affected_rows;
//echo "righe modificate $n_righe";
//echo "<br/>";

	

}


//echo "ciao";

echo json_encode($id_state); 


?>