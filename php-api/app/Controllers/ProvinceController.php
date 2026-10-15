<?php

namespace App\Controllers;

use CodeIgniter\HTTP\ResponseInterface;

class ProvinceController extends BaseController
{
    public function getAllProvinces(): ResponseInterface
    {
        //tableau vide
        $list_provinces = [];
        $count = 0;

        //ouvrir le fichier
        $handle = fopen(WRITEPATH . "provinces", 'r');

        //lire l'en-tete
        $headers = fgetcsv($handle, 0, ';');

        //prendre tout les provinces
        while (($row = fgetcsv($handle, 0, ';')) !== false) {
            $province = array_combine($headers, $row);

            $list_provinces[$count] = $province;
            $count++;
        }

        //conversion en JSON : ** response = le reponse renvoie par le serveur vers le navigateur
        $json = $this->response->setJSON($list_provinces);

        fclose($handle);

        return $json;        

    }
}
