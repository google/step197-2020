import React, { Component } from 'react';
import styled from '@emotion/styled';

const container = {
	width: '30%',
	minWidth: '12rem',
	height: '5rem',
	border: '0.2rem solid #136f9f',
	backgroundColor: 'white',
	color: 'black',
	borderRadius: '0.5rem',
	fontSize: '1.75rem',
};

class LabelScroll extends Component {
	render() {
		const labelItems = this.props.labels.map((label) => {
			return (
				<option key={label} value={label}>
					{label}
				</option>
			);
		});
		return (
			<div
				style={container}
				onChange={this.props.clickFunc}
				value={this.props.selected}
				name="rawText"
				id={this.props.name}
			>
				{labelItems}
			</div>
		);
	}
}

export default LabelScroll;
