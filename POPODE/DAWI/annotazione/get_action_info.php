<?php
//gets the information about an agent, given its id
//get_action_info.php?action=1508

include 'include/connessione.php';

$id_action = $_REQUEST["action"];

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 


$conn->set_charset("latin1");

$sql = "SELECT * FROM Action WHERE idAction = '$id_action'";
$result = $conn->query($sql);if (!($result = mysql_query($sql, $connessione)))
// die("Non riesco a eseguire la query che recupera le precondizioni");
        
//only one agent is expected        
$riga = $result->fetch_assoc(); 		

$dati_action = array();

$dati_action["print"] = utf8_encode($riga["printAction"]);
$dati_action["name"] = utf8_encode($riga["nameAction"]);
$dati_action["id"] = $id_action;
     	
echo json_encode($dati_action);

?>