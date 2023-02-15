<?php
//gets the information about an agent, given its id
//get_goal_info.php?goal=2

include 'include/connessione.php';

$id_goal = $_REQUEST["goal"];

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");		
	 		
$sql = "SELECT * FROM Goal WHERE idGoal = '$id_goal'";
//echo $sql;

$result = $conn->query($sql);
        
//only one agent is expected        
$riga = $result->fetch_assoc(); 		

$dati_goal = array();

$dati_goal["print"] = utf8_encode($riga["printGoal"]);
$dati_goal["name"] = utf8_encode($riga["nameGoal"]);
$dati_goal["id"] = $id_goal;
     	
echo json_encode($dati_goal);

?>