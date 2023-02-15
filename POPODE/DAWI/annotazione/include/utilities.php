<?php

include '../include/connessione.php';

//input: unit id; output: assoc array of unit metadata
function leggiMetadatiUnit ($idunit,$connessione) {

$sql = "SELECT * FROM `unit` WHERE `idunit` = '$idunit';";
//echo $sql;

if (!($result = mysqli_query($sql, $connessione)))
	 die("Non riesco a leggere i dati della unit");

if (mysqli_num_rows($result) == 0) die("Questa clip non esiste");

$dati_unit = [];

$unit = mysqli_fetch_array($result);

//title URI notes initial
$title = $unit["title"];
$dati_unit["title"] = $title;

$id = $unit["idunit"];
$dati_unit["idunit"] = $id;

$notes = $unit["notes"];
$dati_unit["notes"] = $notes;

$clip = $unit["URI"];
$dati_unit["clip"] = $clip;

$initial = $unit["initial"];
$dati_unit["initial"] = $initial;

$final = $unit["final"];
$dati_unit["final"] = $final;

return $dati_unit;

}

//leggo le emozioni della unit

function leggiEmozioniUnit ($idunit,$connessione){

$array_emotions = array("amusement" => 0, "pride" => 0, "joy" => 0, "relief" => 0, "interest" => 0, "pleasure" => 0, "hot anger" => 0, "panic fear" => 0, "despair" => 0,  "irritation" => 0, "anxiety" => 0, "sadness" => 0);

if (isset($idunit)){

	$sql = "SELECT * FROM `unit_has_emotion`JOIN `emotion` ON `emotion`.`idemotion` = `unit_has_emotion`.`id_emotion` WHERE `id_unit` = '$_REQUEST[unit]';";

	if (!($result = mysqli_query($sql, $connessione)))
	 die("Non riesco a leggere le emozioni della clip");

	//aggiungo tutte le emozioni presenti
	while($e = mysqli_fetch_array($result)) {
		$nome_emozione = $e["emotion name"];
		$array_emotions[$nome_emozione] = 1;
	}

}
return $array_emotions;

}

?>
