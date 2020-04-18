<?php

	system("cmd/c C:\\xampp\\htdocs\\docker\\first_dog_or_cat\\test1.bat test.jpg");

    $array_dogname = file("a.txt");//讀進辨識的結果
    $dogname=$array_dogname[0];//第一行
 	$dogname0=trim($dogname);
 	$dogname1=$array_dogname[1];//第二行
    $dogname2=trim($dogname1);
    // echo  $dogname0;
 	#echo $array[1];
 	for ($i=0; $i <5; $i++) {
	    $dir='C:/xampp/htdocs/docker/third_who_dog/'. $dogname0 .'/tf_files/lose/'.$dogname2;
	    $file=scandir($dir);
	    $a=rand(3,10);
	    print_r($file[$a]."/");
    }   
?>




