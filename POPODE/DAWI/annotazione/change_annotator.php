
<?php

// input: unit id, new annotator

// cerca id vecchio annotatore where unit
// inserisce nuovo annotatore 
// update vecchio annotatore in unit
// cancella vecchio annotatore


include 'include/connessione.php';

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

$annotator = addslashes($_REQUEST["annotator"]);
$unit = $_REQUEST["unit"];

//recupero id vecchio annotatore
$sql = "SELECT Annotator_idAnnotator FROM Unit_Annotator WHERE Unit_idUnit = $unit";
//echo $sql;

$result = $conn->query($sql);            
$riga = $result->fetch_assoc();
$old_annotator = $riga["Annotator_idAnnotator"];  



// aggiungo nuovo annotatore
$sql = "INSERT INTO `Annotator` (`Name`) VALUES ('$annotator')";
if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}
$new_annotator = $last_id;


// modifico annotatore della Unit
$sql = "UPDATE `Unit_Annotator` SET `Annotator_idAnnotator`= $new_annotator WHERE `Unit_idUnit` = $unit";
if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

// ora cancello vecchio annotatore
$sql = "DELETE FROM `Annotator` WHERE idAnnotator = $old_annotator ";
if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}


echo json_encode("ok");






























?>

