
var EditorTemplates = {
	team: {
		"Name":          { name: "name", type: "text" },
		"Nickname":      { name: "nickname", type: "text" },
		"Number":        { name: "number", type: "number" },
		"Country":       { name: "country", type: "text" },
		"State":         { name: "state", type: "text" },
		"City":          { name: "city", type: "text" },
		"Rookie Season": { name: "rookieSeason", type: "number" },
		"Motto":         { name: "motto", type: "text" },
		"Website":       { name: "website", type: "text" }
	},
	
	event: {
		"Name":       { name: "name", type: "text" },
		"Short Name": { name: "shortName", type: "text"},
		"Identifier": { name: "identifier", type: "text" },
		"Venue":      { name: "venue", type: "text" },
		"City":       { name: "city", type: "text" },
		"State":      { name: "state", type: "text" },
		"Country":    { name: "country", type: "text" }
	},
	
	game: {
		"Event Name":    { name: "shortName", type: "text" },
		"Game Year":     { name: "gameYear", type: "text" },
		"EID":           { name: "eid", type: "number" },
		"Start Date":    { name: "startDate", type: "date" },
		"End Date":      { name: "endDate", type: "date" },
		"Results URL":   { name: "resultsURL", type: "url" },
		"Standings URL": { name: "standingsURL", type: "url" },
		"Awards URL: ":  { name: "awardsURL", type: "url" }
	}
}
