<?php

//get_agents.php (no parametri)
//restituisce un array di agents, ogni agent è un array associativo con tutte le caratteristiche

include 'include/connessione.php';

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

//prima recupero dalla tabella Unit la timeline della Unit in questione
$sql = "SELECT * FROM Agent ORDER BY `nameAgent`";
$result = $conn->query($sql);

$agents = array();

while($row = $result->fetch_assoc()) {
	    // un array associativo per ogni piano
    	$agent = array();
        $agent["id"] = $row["idAgent"];
        $agent["print"] = utf8_encode($row["printAgent"]);
        $agent["name"] = utf8_encode($row["nameAgent"]);
   
        $agents[] = $agent;  
    }

$conn->close();

echo json_encode($agents);






?>