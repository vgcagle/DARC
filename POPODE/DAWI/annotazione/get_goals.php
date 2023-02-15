<?php

//get_goals.php (no parametri)
//restituisce un array di goal, ogni goal è un array associativo con tutte le caratteristiche

include 'include/connessione.php';

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

//prima recupero dalla tabella Unit la timeline della Unit in questione
$sql = "SELECT * FROM Goal ORDER BY `nameGoal`";
$result = $conn->query($sql);

$goals = array();

while($row = $result->fetch_assoc()) {
	    // un array associativo per ogni piano
    	$goal = array();
        $goal["id"] = $row["idGoal"];
        $goal["print"] = utf8_encode($row["printGoal"]);
        $goal["name"] = utf8_encode($row["nameGoal"]);
   
        $goals[] = $goal;  
    }

$conn->close();

echo json_encode($goals);






?>