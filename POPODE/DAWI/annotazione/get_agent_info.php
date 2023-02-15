<?php
//gets the information about an agent, given its id
//get_agent_info.php?agent=109

include 'include/connessione.php';

$id_agent = $_REQUEST["agent"];

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");
	 			
	 		
$sql = "SELECT * FROM Agent WHERE idAgent = '$id_agent'";
$result = $conn->query($sql);
        
//only one agent is expected        
$riga_agente = $result->fetch_assoc();	

//recupero i suoi values dalla tabella apposita
$sql = "SELECT * FROM Value WHERE Agent_idAgent = '$id_agent'";
$result = $conn->query($sql);
	
$stringa_values = "";		 	
while ($riga_value =  $result->fetch_assoc()) {		 	
	$stringa_values = $stringa_values .  $riga_value["nameValue"] . ", ";
	
}

//recupero i suoi liking dalla tabella apposita
$sql = "SELECT * FROM Attitude WHERE Attitude_idAgent = '$id_agent' AND typeAttitude = 'liking'";
$result = $conn->query($sql);
	
$stringa_liking = "";		 	
while ($riga_liking = $result->fetch_assoc()) {		 	
	$stringa_liking = $stringa_liking .  $riga_liking["objectAttitude"] . ", ";
	
}

//recupero i suoi disliking dalla tabella apposita
$sql = "SELECT * FROM Attitude WHERE Attitude_idAgent = '$id_agent' AND typeAttitude = 'disliking'";
$result = $conn->query($sql);
	
$stringa_disliking = "";		 	
while ($riga_disliking = $result->fetch_assoc()) {		 	
	$stringa_disliking = $stringa_disliking .  $riga_disliking["objectAttitude"] . ", ";
	
}




/*
//recupero i suoi pleasant dalla tabella apposita
$sql = "SELECT * FROM Attitude WHERE Attitude_idAgent = '$id_agent' AND typeAttitude = 'pleasant'";
if (!($result = mysql_query($sql, $connessione)))
		 	die("Non riesco a eseguire la query che recupera i pleasant");
	
$stringa_pleasant = "";		 	
while ($riga_pleasant = mysql_fetch_array($result)) {		 	
	$stringa_pleasant = $stringa_pleasant .  $riga_pleasant["objectAttitude"] . ", ";
	
}
*/


$stringa_values = rtrim($stringa_values, ", ");
$stringa_liking = rtrim($stringa_liking, ", ");
$stringa_disliking = rtrim($stringa_disliking, ", ");
//$stringa_pleasant = rtrim($stringa_pleasant, ", ");

$dati_agent = array();


$dati_agent["print"] = utf8_encode($riga_agente["printAgent"]);
$dati_agent["name"] = utf8_encode($riga_agente["nameAgent"]);
$dati_agent["pleasant"] = $riga_agente["pleasantAgent"];
$dati_agent["values"] = utf8_encode($stringa_values);
$dati_agent["id"] = $id_agent;
$dati_agent["liking"] =  utf8_encode($stringa_liking);
$dati_agent["disliking"] =  utf8_encode($stringa_disliking);
//$dati_agent["pleasant"] =  utf8_encode($stringa_pleasant);
     	
echo json_encode($dati_agent);

?>