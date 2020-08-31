import React from 'react';
import './Folder.css';
import { Link } from 'react-router-dom';
import deleteIcon from '../SVG/delete-24px.svg';
import editIcon from '../SVG/edit-24px.svg';
import { motion } from 'framer-motion';

class Folder extends React.Component {
	constructor(props) {
		super(props);
		this.handleDeleteCard = this.handleDeleteCard.bind(this);
		this.handleEditCard = this.handleEditCard.bind(this);
	}

	handleDeleteCard(e) {}

	handleEditCard(e) {}

	render() {
		return (
			<div className="folder" key={this.props.folderURL}>
				<Link to={`/insideFolder?folderKey=${this.props.folderURL}`}>
					<div className="FolderContent">
						<br />
						<p style={{ textDecoration: 'underline' }}>{this.props.name}</p>
						<p>{this.props.language}</p>
					</div>
				</Link>
				<div className="icons">
					<div className="edit" onClick={this.handleEditCard}>
						<motion.div whileHover={{ scale: 1.2 }}>
							<img src={editIcon} alt="icon option"></img>
						</motion.div>
					</div>
					<div className="delete" onClick={this.handleDeleteCard}>
						<motion.div whileHover={{ scale: 1.2 }}>
							<img src={deleteIcon} alt="icon option"></img>
						</motion.div>
					</div>
				</div>
			</div>
		);
	}
}

export default Folder;
