<?php

include 'include/connessione.php';
		
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 



$where =  "";

$conn->set_charset("latin1");

if (isset ($_REQUEST["type"])) {	

	$type = $_REQUEST["type"];

	if ($type == "onlyunits") {
		
		$where = "WHERE isReference = 'annotation' ";
		
		} 
		
		elseif ($type == "onlyrefs") 
		{
			$where = "WHERE isReference = 'reference' ";
	
		} else { $where = ""; }
}



$sql = "SELECT * FROM Unit " . $where . " ORDER BY `printUnit`";

$result = $conn->query($sql);

 
$array_units =  array(); 		
 
while ($riga = $result->fetch_assoc()) { 		
		$id_unit = $riga["idUnit"];   
		$print_unit = utf8_encode($riga["printUnit"]); 
		$is_reference = $riga["isReference"];
		$array_dati = array();
		$array_dati["id"]= $id_unit; 
		$array_dati["print"]= $print_unit; 
		$array_dati["ref"]= $is_reference;
		$array_units[] = $array_dati;

    }



echo json_encode($array_units); 

?>