<?php
header("Content-Type:text/html; charset=utf-8");
$dbhost = 'localhost';
$dbuser = 'happydog';
$dbpass = 'happy12354';
$dbname = 'app_dogface';
$conn = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);
mysqli_query($conn, "SET NAMES UTF8"); //$con, sql

    $did = $_POST['did'];
    $pid = $_POST['pid'];
    $varpath = $_POST['myvariety'];
    $filename = "D$did"."_$pid";
    // $did = "28";
    // $pid = "20";
    $file ="";
    $a = glob("C:/xampp/htdocs/docker/third_who_dog/".$varpath."/tf_files/train/".$filename."/*.*");
    $allfile = count($a);

    $dir = "C:/xampp/htdocs/docker/third_who_dog/".$varpath."/tf_files/train/".$filename."/";
    $sql = "SELECT * FROM `lost_table`  where p_id='$pid' && d_id='$did'";//查詢整個表單 ex.' $ name'
    $result = mysqli_query($conn,$sql) ;//查詢
    $nums = mysqli_num_rows($result);
    if($nums>0){
      echo "1";
    }else{
      //http://www.phpernote.com/php-function/1101.html
      //判斷文件夾是否存在
      if(!is_dir($dir)){
        echo "2";
      }else{
        //判斷該資料夾上傳圖片是否多張
        if($allfile <= 3){
          echo "3";
        }
      }
    }
    mysqli_close($conn);

?>
