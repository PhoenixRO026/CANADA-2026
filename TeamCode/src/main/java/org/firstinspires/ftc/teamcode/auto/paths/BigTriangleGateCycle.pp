{
  "startPoint": {
    "x": 18,
    "y": 118,
    "heading": "linear",
    "startDeg": 144,
    "endDeg": 180,
    "locked": false
  },
  "lines": [
    {
      "id": "line-scorePreload",
      "name": "ScorePreload",
      "endPoint": {
        "x": 40,
        "y": 95,
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
        "x": 20,
        "y": 82.5,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [
        {
          "x": 47.34,
          "y": 80.61
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
        "x": 44.5,
        "y": 82.5,
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
        "x": 17,
        "y": 58,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [
        {
          "x": 48,
          "y": 50
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
        "x": 44.5,
        "y": 82.5,
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
        "x": 23,
        "y": 56,
        "heading": "linear",
        "reverse": false,
        "startDeg": 180,
        "endDeg": 145
      },
      "controlPoints": [
        {
          "x": 35.071,
          "y": 60.34
        }
      ],
      "color": "#2563eb",
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
        "x": 20,
        "y": 50,
        "heading": "constant",
        "reverse": false,
        "degrees": 145
      },
      "controlPoints": [
        {
          "x": 34,
          "y": 48
        }
      ],
      "color": "#dc2626",
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
        "x": 44.5,
        "y": 82.5,
        "heading": "linear",
        "reverse": false,
        "startDeg": 145,
        "endDeg": 180
      },
      "controlPoints": [],
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
        {
          "x": 141.5,
          "y": 70
        },
        {
          "x": 141.5,
          "y": 141.5
        },
        {
          "x": 120,
          "y": 141.5
        },
        {
          "x": 138,
          "y": 119
        },
        {
          "x": 138,
          "y": 70
        }
      ],
      "color": "#dc2626",
      "fillColor": "#ff6b6b"
    },
    {
      "id": "triangle-2",
      "name": "Blue Goal",
      "vertices": [
        {
          "x": 6,
          "y": 119
        },
        {
          "x": 25,
          "y": 141.5
        },
        {
          "x": 0,
          "y": 141.5
        },
        {
          "x": 0,
          "y": 70
        },
        {
          "x": 6,
          "y": 70
        }
      ],
      "color": "#2563eb",
      "fillColor": "#60a5fa"
    }
  ],
  "sequence": [
    {
      "kind": "path",
      "lineId": "line-scorePreload"
    },
    {
      "kind": "path",
      "lineId": "line-intakeClose"
    },
    {
      "kind": "path",
      "lineId": "line-shootClose"
    },
    {
      "kind": "path",
      "lineId": "line-intakeMiddle"
    },
    {
      "kind": "path",
      "lineId": "line-shootMiddle"
    },
    {
      "kind": "path",
      "lineId": "line-gateApproach"
    },
    {
      "kind": "path",
      "lineId": "line-gateRam"
    },
    {
      "kind": "path",
      "lineId": "line-shootGate"
    }
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
        "line-shootGate"
      ]
    }
  ],
  "version": "1.2.1",
  "timestamp": "2026-07-12T00:28:18.703Z"
}