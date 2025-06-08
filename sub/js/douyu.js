import cheerio from "assets://js/lib/cheerio.min.js";
import "assets://js/lib/crypto-js.js";
import 模板 from "./模板.js";
import {
	gbkTool
}
from "./gbk.js";

function init(ext) {
	console.log("init");
	console.log("init.ext=" + ext);
}

function home(filter) {
	console.log("home");
//  try {
//    console.log("--------home.0");
//    const ts = Math.floor(new Date().getTime() / 1e3);
//    var a = ub98484234("74374", "10000000000000000000000000001501", ts);
////    var finalRoomID = "74374";
////    const did = "10000000000000000000000000001501";
////    const signFunc = `${a}(${finalRoomID},"${did}",${ts})`;
//    console.log("--------home.1");
//    console.log(a);
//    console.log("--------home.2");
////    var b = playPC("74374", a);
////    console.log(b);
////    console.log("--------home.3");
//    return a;
//  } catch(e) {
//    console.log("home执行失败:" + e.message)
//  }
	return null;
}

function homeVod(params) {
	console.log("homeVod");
	return null
}

function category(tid, pg, filter, extend) {
  console.log("category");
	return null;
}

function detail(vod_url) {
  console.log("detail");
	return null;
}

function play(htmlJs, id, flags) {
  console.log("play");
  try {
//    console.log("--------play.0");
    const did = "10000000000000000000000000001501";
    const ts = Math.floor(new Date().getTime() / 1e3);
//    var a = ub98484234(id, did, ts);
//    console.log("--------play.1");
//    console.log(a);
    console.log("--------play.id="+id);
//    console.log("--------play.2");
    let htmlJsCall = 'ub98484234("'+id+'", "'+did+'", '+ts+');';
    var program = eval(htmlJs + htmlJsCall);
    console.log("--------play.program="+program);
//    console.log("--------play.3");
    return program;
  } catch(e) {
    console.log("play执行失败:" + e.message)
  }
	return null;
}

function search(wd, quick, pg) {
  console.log("search");
	return null;
}

function proxy(params) {
  console.log("proxy");
	return null;
}

function sniffer() {
  console.log("sniffer");
	return null;
}

function isVideo(url) {
  console.log("isVideo");
	return null;
}

function DRPY() {
	return {
		init: init,
		home: home,
		homeVod: homeVod,
		category: category,
		detail: detail,
		play: play,
		search: search,
		proxy: proxy,
		sniffer: sniffer,
		isVideo: isVideo
	}
}

export default {
  init: init,
  home: home,
  homeVod: homeVod,
  category: category,
  detail: detail,
  play: play,
  search: search,
  proxy: proxy,
  sniffer: sniffer,
  isVideo: isVideo,
  DRPY: DRPY
};
  
