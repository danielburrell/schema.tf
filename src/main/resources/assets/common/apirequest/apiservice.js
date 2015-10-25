angular.module('apiservice', [ 'ngResource' ]).service(
		"hatf2api",
		[
				'$http',
				'$q',
				function(http, q) {
					return {
						
						getUserInfo : function() {
							var address = q.defer();
							http.get("/api/whoAmI")
									.then(function(data) {
										address.resolve(data);
									}, function(err) {
										address.reject(err);
									});
							return address.promise;
						},
						getSchema : function() {
							var address = q.defer();
							http.get("https://schema.tf/api/getAllItemsCdn", { cache: true})
									.then(function(data) {
										address.resolve(data);
									}, function(err) {
										address.reject(err);
									});
							return address.promise;
						},
						getQualities : function() {
							var address = q.defer();
							http.get("https://schema.tf/api/getAllQualitiesSimple", { cache: true})
									.then(function(data) {
										address.resolve(data);
									}, function(err) {
										address.reject(err);
									});
							return address.promise;
						}
						

					}
				} ]);
