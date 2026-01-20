return {
  version = "1.10",
  luaversion = "5.1",
  tiledversion = "1.11.2",
  class = "",
  orientation = "orthogonal",
  renderorder = "right-down",
  width = 7,
  height = 7,
  tilewidth = 16,
  tileheight = 16,
  nextlayerid = 2,
  nextobjectid = 1,
  properties = {},
  tilesets = {
    {
      name = "tiles_ghost",
      firstgid = 1,
      filename = "tiles_ghost.tsx"
    }
  },
  layers = {
    {
      type = "tilelayer",
      x = 0,
      y = 0,
      width = 7,
      height = 7,
      id = 1,
      name = "图块层 1",
      class = "",
      visible = true,
      opacity = 1,
      offsetx = 0,
      offsety = 0,
      parallaxx = 1,
      parallaxy = 1,
      properties = {},
      encoding = "lua",
      data = {
            1,1,1,1,1,1,1,1,1,
            1,0,49,49,57,49,49,0,1,
            1,49,50,74,5,74,50,49,1,
            1,49,11,11,1,11,11,49,1,
            1,57,1,1,1,1,1,57,1,
            1,49,11,11,1,11,11,49,1,
            1,49,49,74,5,74,49,49,1,
            1,0,49,49,57,49,49,0,1,
            1,1,1,1,1,1,1,1,1
      }
    }
  }
}
