{
  "startPoint": {
    "x": 55,
    "y": 9,
    "heading": "linear",
    "startDeg": 90,
    "endDeg": 90,
    "locked": true
  },
  "lines": [
    {
      "id": "line-scorePreload",
      "name": "ScorePreload",
      "endPoint": {
        "x": 55,
        "y": 12,
        "heading": "linear",
        "startDeg": 90,
        "endDeg": 90
      },
      "controlPoints": [],
      "color": "#7A5A6D",
      "locked": true,
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "id": "line-intakeFar",
      "name": "IntakeFar",
      "endPoint": {
        "x": 12,
        "y": 35,
        "heading": "linear",
        "startDeg": 90,
        "endDeg": 180
      },
      "controlPoints": [
        {
          "x": 45,
          "y": 30
        }
      ],
      "color": "#7A5A6D",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "locked": true
    },
    {
      "id": "line-shootFar",
      "name": "ShootFar",
      "endPoint": {
        "x": 42,
        "y": 9.5,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [],
      "color": "#7A5A6D",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "locked": true
    },
    {
      "id": "line-intakeHuman",
      "name": "IntakeHuman",
      "endPoint": {
        "x": 12,
        "y": 9.5,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [],
      "color": "#7A5A6D",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "locked": true
    },
    {
      "id": "line-kindaBetween",
      "name": "KindaBetween",
      "endPoint": {
        "x": 12,
        "y": 18,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [
        {
          "x": 20,
          "y": 15
        }
      ],
      "color": "#7A5A6D",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "id": "line-shootHuman",
      "name": "ShootHuman",
      "endPoint": {
        "x": 42,
        "y": 9.5,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [
        {
          "x": 20,
          "y": 15
        }
      ],
      "color": "#7A5A6D",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": "",
      "locked": true
    }
  ],
  "shapes": [
    {
      "id": "triangle-1",
      "name": "Red Goal",
      "vertices": [
        { "x": 141.5, "y": 70 },
        { "x": 141.5, "y": 141.5 },
        { "x": 120, "y": 141.5 },
        { "x": 135.677, "y": 118.419 },
        { "x": 135.677, "y": 69.419 }
      ],
      "color": "#dc2626",
      "fillColor": "#ff6b6b"
    },
    {
      "id": "triangle-2",
      "name": "Blue Goal",
      "vertices": [
        { "x": 6, "y": 119 },
        { "x": 25, "y": 141.5 },
        { "x": 0, "y": 141.5 },
        { "x": 0, "y": 70 },
        { "x": 6, "y": 70 }
      ],
      "color": "#2563eb",
      "fillColor": "#60a5fa"
    }
  ],
  "sequence": [
    { "kind": "path", "lineId": "line-scorePreload" },
    { "kind": "path", "lineId": "line-intakeFar" },
    { "kind": "path", "lineId": "line-shootFar" },
    { "kind": "path", "lineId": "line-intakeHuman" },
    { "kind": "path", "lineId": "line-kindaBetween" },
    { "kind": "path", "lineId": "line-shootHuman" }
  ],
  "pathChains": [
    {
      "id": "chain-main",
      "name": "Main Auto Sequence",
      "color": "#7A5A6D",
      "lineIds": [
        "line-scorePreload",
        "line-intakeFar",
        "line-shootFar",
        "line-intakeHuman",
        "line-kindaBetween",
        "line-shootHuman"
      ]
    }
  ],
  "version": "1.2.1",
  "timestamp": "2026-07-19T09:26:52.035Z"
}