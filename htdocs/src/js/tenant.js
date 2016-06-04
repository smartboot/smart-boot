(function() {
	angular.module('tenantModule', ['niceComponent', 'app'])
		.controller('tenantCtrl', ['$scope', '$http', '$pagination', '$url', '$sce', '$alert','$modal', function($scope, $http, $pagination, $url, $sce, $alert,$modal) {

			//租户详情界面
			var tenantDetailModal = $modal({scope: $scope,animation:'am-fade-and-slide-top',templateUrl: 'component/tenant_add_or_edit.tpl.html',show: false});

			$scope.tenant = "";
			$scope.icons = [];
		
			$scope.listMessage = null;
			//租户列表
			$scope.page = $pagination({
				load: function(page) {
					$http.get($url.tenant_list,{
						params:angular.extend({tenant:$scope.searchTenant})
					}).success(function(response) {
						$scope.page.options.currentPage=1;
						if (response.code != 0) {
							$scope.listMessage = response.message;
							$scope.page.dataList = {};
							$scope.page.options.total = 0;
							$scope.page.options.pageSize = 10;
							$scope.page.options.pages = 1;
							return;
						}
						$scope.listMessage = null;
						console.log(response);
						$scope.page.dataList = response.data.data;
						$scope.page.options.total = response.data.total;
						$scope.page.options.pageSize = response.data.pageSize;
						$scope.page.options.pages = Math.ceil(response.data.total / response.data.pageSize);
					}).error(function(response) {
						console.log(response);
					});
				}
			});

			$scope.page.loadPage(1); //加载第一页数据

			//租户详情
			$scope.infoDetail = {

			};
			$scope.isEdit = false;
			/**
			 * 新增/修改租户详情界面
			 * @param {[type]} id [description]
			 */
			$scope.addOrEditDetail = function(code) {
					if (code) {
						$http.get($url.tenant_get, {
							// method: 'POST',
							params: angular.extend({
								code: code
							}),
						}).success(function(response) {
							$scope.isEdit = true;
							console.log(response);
							$scope.infoDetail = response.data;
							tenantDetailModal.$promise.then(tenantDetailModal.show);
						});
					} else {
						$scope.isEdit = false;
						$scope.infoDetail = {};
						tenantDetailModal.$promise.then(tenantDetailModal.show);
					}
				}
				/**
				 * 保存资讯信息
				 * @return {[type]} [description]
				 */
			$scope.submit = function() {
				$http.post($scope.isEdit ? $url.tenant_update : $url.tenant_add, $scope.infoDetail)
					.success(function(response) {
						if (response.code != 0) {
							// alert(response.message);
							$alert({
								title: response.message,
								container: '.modal-footer',
								// content: 'Best check yo self, you\'re not looking too good.',
								placement: 'top-right',
								duration: "3",
								type: 'danger',
								show: true
							});
							return;
						}
						$alert({
							title: '保存成功!',
							container: 'body',
							// content: 'Best check yo self, you\'re not looking too good.',
							placement: 'top-right',
							duration: "3",
							type: 'info',
							show: true
						});
						$scope.page.loadPage(1);
						tenantDetailModal.$promise.then(tenantDetailModal.hide);
					});
			}

			$scope.delete = function(code) {
				if (!code) {
					alert('请指定租户code');
				}
				if(!confirm("确定删除该租户吗?")){
					return;
				}
				$http.get($url.tenant_del, {
					// method: 'POST',
					params: angular.extend({
						code: code,
					}),
				}).success(function(response) {
					if (response.code != 0) {
						// alert(response.message);
						return;
					}
					console.log(response);
					$alert({
						title: '删除成功!',
						container: 'body',
						// content: 'Best check yo self, you\'re not looking too good.',
						placement: 'top-right',
						duration: "3",
						type: 'info',
						show: true
					});
					// alert('删除成功');
					$scope.page.loadPage(1);
				});
			}
		}]);
})();