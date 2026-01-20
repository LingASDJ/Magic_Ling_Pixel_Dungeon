return {
  version = "1.10",
  luaversion = "5.1",
  tiledversion = "1.11.2",
  class = "",
  orientation = "orthogonal",
  renderorder = "right-down",
  width = 10,
  height = 10,
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
      width = 10,
      height = 10,
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
        1, 1, 67, 67, 1, 1, 1, 1, 1, 1,
        1, 67, 51, 49, 59, 49, 51, 51, 1, 1,
        5, 51, 51, 73, 1, 73, 1, 51, 51, 5,
        5, 51, 5, 5, 5, 5, 5, 5, 51, 5,
        5, 51, 5, 51, 51, 51, 51, 5, 51, 5,
        5, 51, 5, 51, 51, 21, 74, 5, 51, 5,
        5, 51, 5, 5, 5, 5, 5, 5, 51, 5,
        5, 51, 51, 1, 1, 1, 1, 51, 51, 5,
        1, 74, 51, 51, 51, 51, 51, 51, 74, 1,
        1, 1, 1, 1, 67, 1, 67, 67, 1, 1
      }
    }
  }
}
