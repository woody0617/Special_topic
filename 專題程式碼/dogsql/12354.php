<?php

   $var = $_POST['myvar'];
   $filename = $_POST['myfile'];
   //$var ="corgi";
   //$filename = "D15_20";
//==============================================
// $img_array = glob("C:/xampp/htdocs/docker/third_who_dog/.$var./tf_files/train/".$filename."/*.{gif,jpg,png}",GLOB_BRACE);
// // $img = array_rand($img_array);
// //echo '<img alt="'.$img_array[$img].'" src="'.$img_array[$img].'" />';
// $j = 3;
// $a =[];
// $b = "";
// for($i=1;$i<$j;$i++){
//     $img = array_rand($img_array);
//     $b = $b."#".$img_array[$img];
// }
// echo $b;    
//==============================================
    $file ="";
    $a = glob("C:/xampp/htdocs/docker/third_who_dog/".$var."/tf_files/train/".$filename."/*.*");
    $allfile = count($a);

    $dir="C:/xampp/htdocs/docker/third_who_dog/".$var."/tf_files/train/".$filename."/";
    $j =5;
    $b = "";
    $c = "";
    if( 5 <= $allfile){
        for ($i=0; $i <$j; $i++) {
	        $file = scandir($dir,1);
	        $b=rand(0,$allfile-1);
            $c = $c."/".$file[$b];
        }
    }else{
        for ($i=0; $i <$allfile; $i++) {
            $file = scandir($dir,1);
            $b=rand(0,$allfile-1);
            $c = $c."/".$file[$b];
        }
    }
 	 
    echo $c;
?>