<?php
//create_goal.php?name= ... &print= ...
///create_goal.php?name=my+goal&print=descrizione+del+my+goal

include 'include/connessione.php';

$name_goal = addslashes($_REQUEST["name"]);
$print_goal = addslashes($_REQUEST["print"]);

/*
$host = "localhost";
$username = "root";
$password = "root";
$db = "DrammarAnnotation";
*/

// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");

$sql = "INSERT INTO Goal (`nameGoal`, `printGoal` ) VALUES ('$name_goal', '$print_goal') ";


// documentation says that last_id deals with concurrency
if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

echo $last_id;

$conn->close();	
	 		

?>