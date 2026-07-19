{
  "startPoint": {
    "x": 18,
    "y": 118,
    "heading": "linear",
    "startDeg": 144,
    "endDeg": 180,
    "locked": true
  },
  "lines": [
    {
      "id": "line-scorePreload",
      "name": "ScorePreload",
      "endPoint": {
        "x": 52,
        "y": 84,
        "heading": "linear",
        "startDeg": 144,
        "endDeg": 180
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
      "id": "line-intakeClose",
      "name": "IntakeClose",
      "endPoint": {
        "x": 22,
        "y": 84,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [
        {
          "x": 43,
          "y": 84
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
      "id": "line-shootClose",
      "name": "ShootClose",
      "endPoint": {
        "x": 59.5,
        "y": 68,
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
      "id": "line-intakeMiddle",
      "name": "IntakeMiddle",
      "endPoint": {
        "x": 19,
        "y": 59,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [
        {
          "x": 48,
          "y": 53
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
      "id": "line-shootMiddle",
      "name": "ShootMiddle",
      "endPoint": {
        "x": 59.5,
        "y": 68,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [
        {
          "x": 35,
          "y": 60
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
      "id": "line-gateApproach",
      "name": "GateApproach",
      "endPoint": {
        "x": 17.5,
        "y": 65,
        "heading": "constant",
        "reverse": false,
        "startDeg": 180,
        "endDeg": 180,
        "degrees": 180
      },
      "controlPoints": [
        {
          "x": 35,
          "y": 65
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
      "id": "line-gateRam",
      "name": "GateRam",
      "endPoint": {
        "x": 14,
        "y": 48,
        "heading": "linear",
        "reverse": false,
        "degrees": 160,
        "startDeg": 180,
        "endDeg": 150
      },
      "controlPoints": [
        {
          "x": 30,
          "y": 53
        },
        {
          "x": 15,
          "y": 58
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
      "id": "mrhwt3ke-2ynukz",
      "name": "GateTurn",
      "endPoint": {
        "x": 13,
        "y": 55,
        "heading": "linear",
        "reverse": false,
        "startDeg": 150,
        "endDeg": 180
      },
      "controlPoints": [
        {
          "x": 18,
          "y": 55
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
      "id": "line-shootGate",
      "name": "ShootGate",
      "endPoint": {
        "x": 59.5,
        "y": 68,
        "heading": "constant",
        "reverse": false,
        "startDeg": 180,
        "endDeg": -90,
        "degrees": 180
      },
      "controlPoints": [
        {
          "x": 37,
          "y": 50
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
        { "x": 138, "y": 119 },
        { "x": 138, "y": 70 }
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
    { "kind": "path", "lineId": "line-intakeClose" },
    { "kind": "path", "lineId": "line-shootClose" },
    { "kind": "path", "lineId": "line-intakeMiddle" },
    { "kind": "path", "lineId": "line-shootMiddle" },
    { "kind": "path", "lineId": "line-gateApproach" },
    { "kind": "path", "lineId": "line-gateRam" },
    { "kind": "path", "lineId": "mrhwt3ke-2ynukz" },
    { "kind": "path", "lineId": "line-shootGate" }
  ],
  "pathChains": [
    {
      "id": "chain-main",
      "name": "Main Chain",
      "color": "#7A5A6D",
      "lineIds": [
        "line-scorePreload",
        "line-intakeClose",
        "line-shootClose",
        "line-intakeMiddle",
        "line-shootMiddle",
        "line-gateApproach",
        "line-gateRam",
        "mrhwt3ke-2ynukz",
        "line-shootGate"
      ]
    }
  ],
  "version": "1.2.1",
  "timestamp": "2026-07-17T19:22:00.000Z"
}