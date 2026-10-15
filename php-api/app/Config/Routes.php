<?php

use CodeIgniter\Router\RouteCollection;

/** @var RouteCollection $routes */
$routes->get('/', 'Home::index');

$routes->get('api/province', 'ProvinceController::getAllProvinces');
$routes->post('api/province', 'ProvinceController::addProvince');