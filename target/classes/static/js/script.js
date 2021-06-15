console.log("This is Script File")


const toggleSidebar = () => {

	if ($(".sidebar").is(":visible")) {
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");

	} else {

		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");

	}
}


/*function validate(){
	
	var fName = document.getElementById("firstName");
	var sName = document.getElementById("secondName");
	var cPhone = document.getElementById("phone");
	var cEmail = document.getElementById("email");
	if(fName.value=="" || sName.value=="" || phone.value=="" || email.value==""){
		alert("Please enter the value");
		return false;
	}
	else{		
		alert("Successfully Added");
		return true;	
	}
}*/

/*Search*/

const search = () => {
	/*console.log("Searching.....");*/

	let query = $("#search-input").val();
	/*console.log(query);*/

	if (query == "") {
		$(".search-result").hide();

	} else {

		console.log(query);

		//Sending request to server

		let url = `http://localhost:8080/search/${query}`;

		fetch(url)
			.then((response) => {
				return response.json();
			})
			.then((data) => {
				//Access data to access JSON
				/*console.log(data);*/
				
				
				//Generate HTML from response data
				let text=`<div class='list-group'>`;
				
				data.forEach((contact) => {
					text += `<a href='/user/${contact.cId}/contact' class='list-group-item list-group-action'> ${contact.name} </a>`
					
				});
				
				
				
				text += `</div>`;
				$(".search-result").html(text);
				$(".search-result").show();

			});
	
	}
};

