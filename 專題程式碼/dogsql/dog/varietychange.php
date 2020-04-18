<?php
    set_time_limit(0);
    header("Content-Type:text/html; charset=utf-8");
    $dbhost = 'localhost';
    $dbuser = 'happydog';
    $dbpass = 'happy12354';
    $dbname = 'app_dogface';
    $conn = mysqli_connect($dbhost, $dbuser, $dbpass,$dbname) ;//連接資料庫
    mysqli_query($conn, "SET NAMES UTF8"); //$con, sql
    $pid = $_POST['pid'];
    $did = $_POST['did'];
    $variety = $_POST['myvariety'];
    $varch = $_POST['myvarch'];


    $sql = "SELECT * FROM `dog_table`  where p_id='$pid' && d_id='$did'";
    $result = mysqli_query($conn,$sql) ;//查詢
    while($e=mysqli_fetch_assoc($result)){
      $output[]=$e;
    }
    $a = $output[0]['d_variety'];
    if ($a == '柯基'){
      $a ="corgi";
    }else if($a == '哈士奇'){
      $a ="husky";
    }else{
      $a ="shibainu";
    }

    $sql = "UPDATE `dog_table` SET `d_variety` = '$variety' where d_id='$id'";//修改資料表單 ex.' $ name'
    mysqli_query($conn,$sql) ;
   //查詢回傳更新資料
//    $sql = "SELECT * FROM `dog_table`  where d_id='$id'";
//    $result = mysqli_query($conn,$sql) ;//查詢
//    while($e=mysqli_fetch_assoc($result)){
//      $output[]=$e;
//    }
//    echo json_encode($output[0]);

   mysqli_close($conn);


    $pathname = "D$did"."_$pid";
    $path1="C:/xampp/htdocs/docker/third_who_dog/".$a."/tf_files/train/".$pathname;
    $path2="C:/xampp/htdocs/docker/third_who_dog/".$varch."/tf_files/lose/".$pathname;
    
    function smartCopy($source, $dest , $options=array('folderPermission'=>0755,'filePermission'=>0755))
    {
        $result=false;
 
        if (is_file($source)) {
            if ($dest[strlen($dest)-1]=='/') {
                if (!file_exists($dest)) {
                    cmfcDirectory::makeAll($dest,$options['folderPermission'],true);
                }
                $__dest=$dest."/".basename($source);
            } else {
                $__dest=$dest;
            }
            $result=copy($source, $__dest);
            chmod($__dest,$options['filePermission']);
 
        } elseif(is_dir($source)) {
            if ($dest[strlen($dest)-1]=='/') {
                if ($source[strlen($source)-1]=='/') {
                    //Copy only contents
                } else {
                    //Change parent itself and its contents
                    $dest=$dest.basename($source);
                    @mkdir($dest);
                    chmod($dest,$options['filePermission']);
                }
            } else {
                if ($source[strlen($source)-1]=='/') {
                    //Copy parent directory with new name and all its content
                    @mkdir($dest,$options['folderPermission']);
                    chmod($dest,$options['filePermission']);
                } else {
                    //Copy parent directory with new name and all its content
                    @mkdir($dest,$options['folderPermission']);
                    chmod($dest,$options['filePermission']);
                }
            }
 
            $dirHandle=opendir($source);
            while($file=readdir($dirHandle))
            {
                if($file!="." && $file!="..")
                {
                     if(!is_dir($source."/".$file)) {
                        $__dest=$dest."/".$file;
                    } else {
                        $__dest=$dest."/".$file;
                    }
                    //echo "$source/$file ||| $__dest<br />";
                    $result=smartCopy($source."/".$file, $__dest, $options);
                }
            }
            closedir($dirHandle);
 
        } else {
            $result=false;
        }
        return $result;
    }

    chmod(0755);
    smartCopy($path1,$path2);
    unlink("C:/xampp/htdocs/docker/third_who_dog/".$a."/tf_files/train/".$pathname);
    
?>