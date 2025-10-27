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
        25, 25, 25, 1, 1, 1, 25, 25, 25, 25,
        25, 25, 81, 81, 81, 81, 81, 81, 25, 25,
        25, 81, 81, 0, 0, 0, 0, 81, 81, 25,
        1, 81, 25, 25, 25, 25, 25, 21, 81, 25,
        1, 59, 11, 11, 11, 11, 1, 1, 81, 1,
        1, 81, 25, 25, 25, 25, 1, 67, 81, 1,
        67, 81, 25, 25, 25, 25, 67, 67, 81, 0,
        1, 81, 81, 25, 25, 67, 67, 81, 81, 0,
        67, 67, 81, 81, 81, 81, 81, 81, 0, 0,
        1, 67, 67, 1, 1, 67, 1, 0, 0, 1
      }
    }
  }
}
