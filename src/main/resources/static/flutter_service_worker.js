'use strict';
const MANIFEST = 'flutter-app-manifest';
const TEMP = 'flutter-temp-cache';
const CACHE_NAME = 'flutter-app-cache';

const RESOURCES = {"assets/AssetManifest.bin": "3262c03b9fbf63b1d37bbb146dcbf70a",
"assets/AssetManifest.bin.json": "8283a1316de6885ea92fda9b5187dd44",
"assets/assets/fonts/01_NTSomic-Regular.ttf": "0effddaadb94e3b4401a0f8e137db4b2",
"assets/assets/fonts/02_NTSomic-Medium.ttf": "2582c6952d56c6c929ab1e9fb5d72c53",
"assets/assets/fonts/03_NTSomic-Bold.ttf": "08224817272d0caa47b153ddc473afc7",
"assets/assets/icons/arrow-down.svg": "aa72f405b9fe739506820ef4f65bed7f",
"assets/assets/icons/arrow-left-dark.svg": "7480e401e26009fae89ea167a48d1e5b",
"assets/assets/icons/arrow-left.svg": "bc718894ef0b5400c00a75934f1bfde6",
"assets/assets/icons/arrow-right.svg": "cf3e6af1c274adbf75ff497c7afa9cb0",
"assets/assets/icons/arrow-up.svg": "c0401fd1cde76a598ebbb1f5ce928b98",
"assets/assets/icons/camera.svg": "dd3fc936e1e3844e4fb80e0a51f04fdf",
"assets/assets/icons/check.svg": "3898e8d12109d10dfddbfd65cd48963a",
"assets/assets/icons/circle-close-light.svg": "2fce02285b13a3406015e3d2f3a56805",
"assets/assets/icons/circle.svg": "b34d9cbc22c90dbc5d1ea34d27ea4879",
"assets/assets/icons/cirlce-close.svg": "c56dfe52a9d9c85cb6c394a6dee57ee3",
"assets/assets/icons/close.svg": "2ac25af964fe5aebe52bf953a377e94b",
"assets/assets/icons/compass.svg": "4af4011b73f3cc71bdc58a28c51e38a4",
"assets/assets/icons/connection-offline.svg": "70484e96869c53c25553ee4434acded5",
"assets/assets/icons/discs.svg": "63477c5331b0234dd06bd416372fa33d",
"assets/assets/icons/dot-filled.svg": "5607a9fc7268b7999573bf92cb4a2edb",
"assets/assets/icons/filters.svg": "c34797166ec0809dfd3fa1bc2b9168e0",
"assets/assets/icons/folder.svg": "db0d360841c13d118891762bbb93a1dc",
"assets/assets/icons/heart-cracked.svg": "d0a6e8486f8beddf28ae5a7c734fff4d",
"assets/assets/icons/heart-selected.svg": "8abed6c50621e338dfd1984d6d8799b5",
"assets/assets/icons/heart.svg": "ba18ef200b135d8b3c64883d39c14d0a",
"assets/assets/icons/home.svg": "bbfefe10ada0d7078fb5f97a61c39104",
"assets/assets/icons/logout.svg": "b21760a4f5320d1daba10e8b0415b60b",
"assets/assets/icons/menu.svg": "33d02b6f5327cb1db0af7aae0cf7f08e",
"assets/assets/icons/no-data.svg": "87b672009c21e816045293201ed57907",
"assets/assets/icons/notification.svg": "0d8bb76dfe07c4f9f59ba39dc25cdcd9",
"assets/assets/icons/parameters.svg": "0620d838ebe7be64ed8c754b619490b2",
"assets/assets/icons/pencil.svg": "a36c0dc684cfe1771daebad839bbcfb7",
"assets/assets/icons/pin-filled.svg": "0d1c84608bacf79078a774549b360ead",
"assets/assets/icons/pin.svg": "97d752cd8d1eea9a9502656daa24ec6f",
"assets/assets/icons/placeholder.png": "f4ad18f5c48499adeada580d5a41b075",
"assets/assets/icons/plus.svg": "69953d9abeaf3dc393137b02fc8ebe65",
"assets/assets/icons/search.svg": "0c9c049f29e092de1fb1f11684e9a6fd",
"assets/assets/icons/setting.svg": "c0dc3d6ae02f8ca5930036864bf517b9",
"assets/assets/icons/share.svg": "807a05965ad3d0b6956270bcdc670f1b",
"assets/assets/icons/time-clock.svg": "884a302c4be516b69e7ba31cde6ed635",
"assets/assets/icons/trash.svg": "892785432e422262eef96585759819ae",
"assets/FontManifest.json": "05ab1a40249f4b5322b3bdfad257f494",
"assets/fonts/MaterialIcons-Regular.otf": "7c2585063a55f974c127c19efe7f6004",
"assets/NOTICES": "09b70a7ecb6784a850b5998fec5fd9a9",
"assets/packages/cupertino_icons/assets/CupertinoIcons.ttf": "33b7d9392238c04c131b6ce224e13711",
"assets/packages/flutter_map/lib/assets/flutter_map_logo.png": "208d63cc917af9713fc9572bd5c09362",
"assets/shaders/ink_sparkle.frag": "ecc85a2e95f5e9f53123dcaf8cb9b6ce",
"assets/shaders/stretch_effect.frag": "40d68efbbf360632f614c731219e95f0",
"canvaskit/canvaskit.js": "8331fe38e66b3a898c4f37648aaf7ee2",
"canvaskit/canvaskit.js.symbols": "a3c9f77715b642d0437d9c275caba91e",
"canvaskit/canvaskit.wasm": "9b6a7830bf26959b200594729d73538e",
"canvaskit/chromium/canvaskit.js": "a80c765aaa8af8645c9fb1aae53f9abf",
"canvaskit/chromium/canvaskit.js.symbols": "e2d09f0e434bc118bf67dae526737d07",
"canvaskit/chromium/canvaskit.wasm": "a726e3f75a84fcdf495a15817c63a35d",
"canvaskit/skwasm.js": "8060d46e9a4901ca9991edd3a26be4f0",
"canvaskit/skwasm.js.symbols": "3a4aadf4e8141f284bd524976b1d6bdc",
"canvaskit/skwasm.wasm": "7e5f3afdd3b0747a1fd4517cea239898",
"canvaskit/skwasm_heavy.js": "740d43a6b8240ef9e23eed8c48840da4",
"canvaskit/skwasm_heavy.js.symbols": "0755b4fb399918388d71b59ad390b055",
"canvaskit/skwasm_heavy.wasm": "b0be7910760d205ea4e011458df6ee01",
"favicon.png": "5dcef449791fa27946b3d35ad8803796",
"flutter.js": "24bc71911b75b5f8135c949e27a2984e",
"flutter_bootstrap.js": "d7afe012dc55dc5b31a7b272c274e563",
"icons/Icon-192.png": "ac9a721a12bbc803b44f645561ecb1e1",
"icons/Icon-512.png": "96e752610906ba2a93c65f8abe1645f1",
"icons/Icon-maskable-192.png": "c457ef57daa1d16f64b27b786ec2ea3c",
"icons/Icon-maskable-512.png": "301a7604d45b3e739efc881eb04896ea",
"index.html": "b41715b723123302e23164831efc6283",
"/": "b41715b723123302e23164831efc6283",
"main.dart.js": "e10200560060262e1012a9fcda37bdff",
"manifest.json": "c91fd555ea0efc26bc0a801920839e16",
"version.json": "80bf5bc3ba2a6c88f943a5607240abb6"};
// The application shell files that are downloaded before a service worker can
// start.
const CORE = ["main.dart.js",
"index.html",
"flutter_bootstrap.js",
"assets/AssetManifest.bin.json",
"assets/FontManifest.json"];

// During install, the TEMP cache is populated with the application shell files.
self.addEventListener("install", (event) => {
  self.skipWaiting();
  return event.waitUntil(
    caches.open(TEMP).then((cache) => {
      return cache.addAll(
        CORE.map((value) => new Request(value, {'cache': 'reload'})));
    })
  );
});
// During activate, the cache is populated with the temp files downloaded in
// install. If this service worker is upgrading from one with a saved
// MANIFEST, then use this to retain unchanged resource files.
self.addEventListener("activate", function(event) {
  return event.waitUntil(async function() {
    try {
      var contentCache = await caches.open(CACHE_NAME);
      var tempCache = await caches.open(TEMP);
      var manifestCache = await caches.open(MANIFEST);
      var manifest = await manifestCache.match('manifest');
      // When there is no prior manifest, clear the entire cache.
      if (!manifest) {
        await caches.delete(CACHE_NAME);
        contentCache = await caches.open(CACHE_NAME);
        for (var request of await tempCache.keys()) {
          var response = await tempCache.match(request);
          await contentCache.put(request, response);
        }
        await caches.delete(TEMP);
        // Save the manifest to make future upgrades efficient.
        await manifestCache.put('manifest', new Response(JSON.stringify(RESOURCES)));
        // Claim client to enable caching on first launch
        self.clients.claim();
        return;
      }
      var oldManifest = await manifest.json();
      var origin = self.location.origin;
      for (var request of await contentCache.keys()) {
        var key = request.url.substring(origin.length + 1);
        if (key == "") {
          key = "/";
        }
        // If a resource from the old manifest is not in the new cache, or if
        // the MD5 sum has changed, delete it. Otherwise the resource is left
        // in the cache and can be reused by the new service worker.
        if (!RESOURCES[key] || RESOURCES[key] != oldManifest[key]) {
          await contentCache.delete(request);
        }
      }
      // Populate the cache with the app shell TEMP files, potentially overwriting
      // cache files preserved above.
      for (var request of await tempCache.keys()) {
        var response = await tempCache.match(request);
        await contentCache.put(request, response);
      }
      await caches.delete(TEMP);
      // Save the manifest to make future upgrades efficient.
      await manifestCache.put('manifest', new Response(JSON.stringify(RESOURCES)));
      // Claim client to enable caching on first launch
      self.clients.claim();
      return;
    } catch (err) {
      // On an unhandled exception the state of the cache cannot be guaranteed.
      console.error('Failed to upgrade service worker: ' + err);
      await caches.delete(CACHE_NAME);
      await caches.delete(TEMP);
      await caches.delete(MANIFEST);
    }
  }());
});
// The fetch handler redirects requests for RESOURCE files to the service
// worker cache.
self.addEventListener("fetch", (event) => {
  if (event.request.method !== 'GET') {
    return;
  }
  var origin = self.location.origin;
  var key = event.request.url.substring(origin.length + 1);
  // Redirect URLs to the index.html
  if (key.indexOf('?v=') != -1) {
    key = key.split('?v=')[0];
  }
  if (event.request.url == origin || event.request.url.startsWith(origin + '/#') || key == '') {
    key = '/';
  }
  // If the URL is not the RESOURCE list then return to signal that the
  // browser should take over.
  if (!RESOURCES[key]) {
    return;
  }
  // If the URL is the index.html, perform an online-first request.
  if (key == '/') {
    return onlineFirst(event);
  }
  event.respondWith(caches.open(CACHE_NAME)
    .then((cache) =>  {
      return cache.match(event.request).then((response) => {
        // Either respond with the cached resource, or perform a fetch and
        // lazily populate the cache only if the resource was successfully fetched.
        return response || fetch(event.request).then((response) => {
          if (response && Boolean(response.ok)) {
            cache.put(event.request, response.clone());
          }
          return response;
        });
      })
    })
  );
});
self.addEventListener('message', (event) => {
  // SkipWaiting can be used to immediately activate a waiting service worker.
  // This will also require a page refresh triggered by the main worker.
  if (event.data === 'skipWaiting') {
    self.skipWaiting();
    return;
  }
  if (event.data === 'downloadOffline') {
    downloadOffline();
    return;
  }
});
// Download offline will check the RESOURCES for all files not in the cache
// and populate them.
async function downloadOffline() {
  var resources = [];
  var contentCache = await caches.open(CACHE_NAME);
  var currentContent = {};
  for (var request of await contentCache.keys()) {
    var key = request.url.substring(origin.length + 1);
    if (key == "") {
      key = "/";
    }
    currentContent[key] = true;
  }
  for (var resourceKey of Object.keys(RESOURCES)) {
    if (!currentContent[resourceKey]) {
      resources.push(resourceKey);
    }
  }
  return contentCache.addAll(resources);
}
// Attempt to download the resource online before falling back to
// the offline cache.
function onlineFirst(event) {
  return event.respondWith(
    fetch(event.request).then((response) => {
      return caches.open(CACHE_NAME).then((cache) => {
        cache.put(event.request, response.clone());
        return response;
      });
    }).catch((error) => {
      return caches.open(CACHE_NAME).then((cache) => {
        return cache.match(event.request).then((response) => {
          if (response != null) {
            return response;
          }
          throw error;
        });
      });
    })
  );
}
