import React, { useState, Component, useEffect } from 'react';
import Header from '../main-components/Header';
import Sidebar from '../main-components/Sidebar';
import InsideFolderContent from '../main-components/InsideFolderContent';
import queryString from 'query-string';

function InsideFolder(props) {
	const [sideSetting, setSideSetting] = useState('f');
	const handleClick = (e) => {
		if (sideSetting === 'f') {
			setSideSetting('t');
		} else {
			setSideSetting('f');
		}
	};

	let folderKey;
	const values = queryString.parse(props.location.search);
	folderKey = values.folderKey;
	
	return (
		<div className="App">
			<Header id="head" handleClick={handleClick}></Header>
			<div id="main">
				<Sidebar bool={sideSetting}></Sidebar>
				<InsideFolderContent folderKey={folderKey}></InsideFolderContent>
			</div>
		</div>
	);
}

export default InsideFolder;
