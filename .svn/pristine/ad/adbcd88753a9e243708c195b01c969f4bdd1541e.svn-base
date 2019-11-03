(function () {
    var app = angular.module('areaApp', []);
    app.controller('GetAreaCodeController', GetAreaCodeController);
    function GetAreaCodeController($scope, $http) {
        var DISTRICTS;
        $http.get('Json/districts.json').then(function (res) {
            /*������ǻ�ȡ��������*/
            $scope.DISTRICTS = res.data;
        });
        $scope.areaObj = {
            'addressProvince' : undefined,
            'addressCity' : undefined,
            'addressDistrict' : undefined,
            /** ��ȡ���յ������루���嵽�����������ؼ��У� ������cityΪ�ó��еĵ����룬districtΪ�������ĵ�������**/
            'getAreaCode': function (province, city, district) {
                var areaCode;
                if(province && $scope.DISTRICTS[province]) {
                    if (city && !$scope.DISTRICTS[city])
                        areaCode = city;
                    else if (district)
                        areaCode = district;
                    return areaCode;
                }else{
                    return province;
                }
            },

            /**���ݵ��������ȡ��ַ����object**/
            'getAreaName': function (areaCode) {
                areaCode = parseInt(areaCode);
                var provinceCode = parseInt(areaCode / 10000) * 10000;
                var provinceName = $scope.DISTRICTS['100000'][provinceCode];
                var cityCode = parseInt(areaCode / 100) * 100;
                var districtName = '';
                var cityName = '';
                if($scope.DISTRICTS[provinceCode]) {
                    if ($scope.DISTRICTS[cityCode]) {
                        districtName = $scope.DISTRICTS[cityCode][areaCode];
                        cityName = $scope.DISTRICTS[provinceCode][cityCode];
                    } else
                        cityName = $scope.DISTRICTS[provinceCode][areaCode];
                }
                var areaObj = {
                    "areaProvince": provinceName,
                    "areaMunicipality": cityName,
                    "areaDistrict": districtName
                };
                return areaObj;
            }
        };
    }
})();