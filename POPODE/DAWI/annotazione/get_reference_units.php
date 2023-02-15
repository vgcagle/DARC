<?php

include 'include/connessione.php';

// esempio di chiamata
		

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

$sql = "SELECT * FROM Unit WHERE isReference='reference' ORDER BY `printUnit` ";
//echo $sql;

$result = $conn->query($sql);
        
//if (!($result = mysql_query($sql, $connessione)))
//		 die("Non riesco a eseguire la query che recupera le unit disponibili");


$array_units =  array(); 		
 
while ($riga = $result->fetch_assoc()) { 		
		$id_unit = $riga["idUnit"];   
		$print_unit = utf8_encode($riga["printUnit"]); 
		$array_dati = array();
		$array_dati["id"]= $id_unit; 
		$array_dati["print"]= $print_unit; 
		$array_units[] = $array_dati;
    }



echo json_encode($array_units); 

    

?>