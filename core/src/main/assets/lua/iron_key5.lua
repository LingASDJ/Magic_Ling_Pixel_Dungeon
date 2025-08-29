return {
  version = "1.10",
  luaversion = "5.1",
  tiledversion = "1.11.2",
  class = "",
  orientation = "orthogonal",
  renderorder = "right-down",
  width = 9,
  height = 9,
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
      width = 9,
      height = 9,
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
        1, 0, 1, 5, 5, 5, 1, 0, 1,
        0, 0, 49, 49, 49, 49, 49, 0, 0,
        1, 49, 49, 74, 21, 74, 49, 49, 1,
        5, 49, 0, 1, 5, 1, 0, 49, 5,
        5, 49, 0, 0, 5, 0, 0, 49, 5,
        5, 49, 1, 0, 5, 0, 1, 49, 5,
        1, 49, 49, 1, 5, 1, 49, 49, 1,
        0, 0, 49, 49, 59, 49, 49, 0, 0,
        1, 0, 1, 5, 5, 5, 1, 0, 1
      }
    }
  }
}
