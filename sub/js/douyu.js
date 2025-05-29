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

function play(flag, id, flags) {
  console.log("play");
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