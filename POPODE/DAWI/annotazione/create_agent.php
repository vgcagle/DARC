<?php
//create_agent.php?name= ... &print= ...
//create_agent.php?name=Ciccio&print=il+solito


include 'include/connessione.php';

$name_agent = addslashes($_REQUEST["name"]);
$print_agent = addslashes($_REQUEST["print"]);
$values = addslashes($_REQUEST["values"]);
$liking = addslashes($_REQUEST["liking"]);
$disliking = addslashes($_REQUEST["disliking"]);
$pleasant = addslashes($_REQUEST["pleasant"]);

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

$sql = "INSERT INTO Agent (`nameAgent`, `printAgent`, `pleasantAgent` ) VALUES ('$name_agent', '$print_agent', $pleasant) ";


// documentation says that last_id deals with concurrency
if ($conn->query($sql) === TRUE) {
    $agent_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}




if ($values != "type values separated by comma") {
	
	$array_values = explode(",", $values);
	
	foreach ($array_values as $v) {
		
		if ($v != ""){
	
			$v = trim($v);
			$sql = "INSERT INTO Value (`nameValue`, `Agent_idAgent`) VALUES ('$v', '$agent_id')";
			$conn->query($sql);
			}
	}	
}


// idAttitude, typeAttitude, Attitude_idAgent, objectAttitude
/*
if ($pleasant != "type names separated by comma") {
	
	$array_pleasant = explode(",", $pleasant);
	
	foreach ($array_pleasant as $p) {
		
		if ($p != ""){
	
			$p = trim($p);
			$sql = "INSERT INTO Attitude (`typeAttitude`, `Attitude_idAgent`,`objectAttitude`) VALUES ('pleasant', '$agent_id', '$p')";
			$conn->query($sql);
			}
	}	
}
*/

if ($liking != "type names separated by comma") {
	
	$array_liking = explode(",", $liking);
	
	foreach ($array_liking as $l) {
		
		if ($l != ""){
	
			$l = trim($l);
			$sql = "INSERT INTO Attitude (`typeAttitude`, `Attitude_idAgent`,`objectAttitude`) VALUES ('liking', '$agent_id', '$l')";
			$conn->query($sql);
			}
	}	
}

if ($disliking != "type names separated by comma") {
	
	$array_disliking = explode(",", $disliking);
	
	foreach ($array_disliking as $l) {
		
		if ($l != ""){
	
			$l = trim($l);
			$sql = "INSERT INTO Attitude (`typeAttitude`, `Attitude_idAgent`,`objectAttitude`) VALUES ('disliking', '$agent_id', '$l')";
			$conn->query($sql);
			}
	}	
}

echo $agent_id;

$conn->close();	
	 		

?>