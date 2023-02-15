<?php

include 'include/connessione.php';
//create_action_id.php?action=cantare

$name_action = addslashes($_REQUEST["action"]);



//$print_action = $_REQUEST["print"];
//echo "print azione: $print_action";


// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");


//c'è il caso in cui action non deve essere creata perché il campo non è stato valorizzato nell'interfaccia
if ($name_action != ""){

	//INSERT INTO `DrammarAnnotation`.`Action` (`idAction`, `printAction`, `Unit_idUnit`) VALUES (NULL, 'Ofelia scappa su un chopper', '2');
	$sql = "INSERT INTO `Action` (`printAction`, `nameAction`) VALUES ('', '$name_action');";
	//echo $sql;

	if ($conn->query($sql) === TRUE) {
	    $last_id = $conn->insert_id;
	    } else {
	    echo "Error: " . $sql . "<br>" . $conn->error;
	}

} else {$last_id = 0;}


//echo $last_id;

echo json_encode($last_id);

$conn->close();	

?>