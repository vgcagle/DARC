<?php
//add_agent2plan.php?plan=3&agent=5

include 'include/connessione.php';

$id_plan = $_REQUEST["plan"];
$id_agent = $_REQUEST["agent"];


// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");

//$sql = "UPDATE Pair SET followsPair = $id_candidate_unit WHERE idPair = $id_coppia_timeline_0";

$sql = "UPDATE Plan SET Agent_idAgent = '$id_agent' WHERE idPlan = $id_plan ";

// documentation says that last_id deals with concurrency
if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}



?>