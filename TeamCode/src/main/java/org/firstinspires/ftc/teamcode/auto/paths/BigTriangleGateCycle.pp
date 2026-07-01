{
  "startPoint": {
    "x": 17,
    "y": 118,
    "heading": "linear",
    "startDeg": 90,
    "endDeg": 180,
    "locked": false
  },
  "lines": [
    {
      "id": "line-fggf93l3acv",
      "name": "PosShoot1",
      "endPoint": {
        "x": 40.214082503556185,
        "y": 95.19487908961594,
        "heading": "linear",
        "startDeg": 142,
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
      "id": "mr24epu7-hg476c",
      "name": "Intake1",
      "endPoint": {
        "x": 25.246088193456618,
        "y": 82.59743954480795,
        "heading": "constant",
        "reverse": false,
        "startDeg": 0,
        "endDeg": 0,
        "degrees": 180
      },
      "controlPoints": [
        {
          "x": 47.34850640113798,
          "y": 80.61735419630156
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
      "id": "mr24mbay-gu0sig",
      "name": "PosShoot2",
      "endPoint": {
        "x": 44.57183499288762,
        "y": 81.79231863442388,
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
      "id": "mr24nu3k-o1pqhx",
      "name": "",
      "endPoint": {
        "x": 24.843527738264584,
        "y": 58.9935988620199,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [
        {
          "x": 50.23189881170731,
          "y": 57.847911857191114
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
      "id": "mr24ped3-nebcnl",
      "name": "Path 5",
      "endPoint": {
        "x": 44.37055476529161,
        "y": 81.7923186344239,
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
      "id": "mr24r674-b08q2r",
      "name": "Path 6",
      "endPoint": {
        "x": 10.694268504036113,
        "y": 60.24672944359791,
        "heading": "linear",
        "reverse": false,
        "startDeg": 180,
        "endDeg": 140
      },
      "controlPoints": [
        {
          "x": 34.668533607658034,
          "y": 58.327856027648565
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
      "id": "mr24w853-klxf1y",
      "name": "Path 7",
      "endPoint": {
        "x": 44.501422475106686,
        "y": 81.83428165007112,
        "heading": "linear",
        "reverse": false,
        "degrees": 180,
        "startDeg": 140,
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
      "lineId": "line-fggf93l3acv"
    },
    {
      "kind": "path",
      "lineId": "mr24epu7-hg476c"
    },
    {
      "kind": "path",
      "lineId": "mr24mbay-gu0sig"
    },
    {
      "kind": "path",
      "lineId": "mr24nu3k-o1pqhx"
    },
    {
      "kind": "path",
      "lineId": "mr24ped3-nebcnl"
    },
    {
      "kind": "path",
      "lineId": "mr24r674-b08q2r"
    },
    {
      "kind": "path",
      "lineId": "mr24w853-klxf1y"
    }
  ],
  "pathChains": [
    {
      "id": "chain-mr23yy0t-vtroyc",
      "name": "Main Chain",
      "color": "#7A5A6D",
      "lineIds": [
        "line-fggf93l3acv",
        "mr24epu7-hg476c",
        "mr24mbay-gu0sig",
        "mr24nu3k-o1pqhx",
        "mr24ped3-nebcnl",
        "mr24r674-b08q2r",
        "mr24w853-klxf1y"
      ]
    }
  ],
  "settings": {
    "xVelocity": 75,
    "yVelocity": 65,
    "aVelocity": 3.141592653589793,
    "kFriction": 0.1,
    "rWidth": 17.461,
    "rHeight": 15.468,
    "safetyMargin": 1,
    "maxVelocity": 40,
    "maxAcceleration": 30,
    "maxDeceleration": 30,
    "fieldMap": "decode.webp",
    "robotImage": "/robot.png",
    "theme": "dark",
    "showGhostPaths": false,
    "showOnionLayers": false,
    "onionLayerSpacing": 3,
    "onionColor": "#dc2626",
    "onionNextPointOnly": false,
    "showHeadingArrow": false,
    "headingArrowLength": 50,
    "headingArrowColor": "#ffffff",
    "headingArrowThickness": 2,
    "pathOpacity": 1
  },
  "version": "1.2.1",
  "timestamp": "2026-07-01T14:04:19.872Z"
}