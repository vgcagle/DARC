<?php

//metto il piano 41 nel piano 3 dopo il piano 5
//add_subplan.php?father=3&child=41&reference=5


include 'include/connessione.php';

$id_father = $_REQUEST["father"];
$id_child = $_REQUEST["child"];
$reference = $_REQUEST["reference"];


$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 


$conn->set_charset("utf8");

if ($reference == "primo"){
		$cont = 1;
		} else {
	
		// trovo l'ordine della reference 
		$sql = "SELECT * FROM SubplanOf WHERE idFatherPlan = $id_father and idChildPlan = $reference";
		echo $sql;

		$result = $conn->query($sql);

		// IF $row ha almeno una riga, cioè ci sono già altri figli, li sposto in avanti {
		$cont = $result->num_rows; 
		//echo "trovate $cont righe <br/>";
	}

echo "<br/>";

if ($cont > 0) {	
	
	if ($reference != "primo"){
	
		$row = $result->fetch_assoc();	
		
		$reference_order = $row["order"]; 
	
		$order = $reference_order + 1; }
		
		else {
			$reference_order = 0;
			$order = 1;
			}

	$sql = "UPDATE SubplanOf SET `order` =  `order` + 1 WHERE idFatherPlan = $id_father and `order` > $reference_order";
	echo $sql;
	

	
	$conn->query($sql);

	} else {
			
			$order = 1;
			
			}
			
echo "<br/>";			

// } ELSE metti order = 1

$sql = "INSERT INTO SubplanOf (idFatherPlan, idChildPlan, `order`) VALUES ('$id_father', '$id_child', $order)";
echo $sql;

//echo $sql;


$conn->query($sql);

$conn->close();	




?>