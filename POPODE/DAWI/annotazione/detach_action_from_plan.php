<?php
//detach_agent_from_plan.php?action=7&plan=3

include 'include/connessione.php';

$id_action = $_REQUEST["action"];
$id_plan = $_REQUEST["plan"];


// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 


$conn->set_charset("utf8");

$sql = "UPDATE Plan SET Action_idAction = null WHERE idPlan = $id_plan ";

// documentation says that last_id deals with concurrency
if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();

?>